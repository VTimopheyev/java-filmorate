package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FilmDbStorage implements FilmStorage {

    private final static Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean filmExists(Integer id) {

        String sqlQuery = "select count(*) from films where film_id = ?";
        Integer result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);

        return result == 1;
    }

    public boolean likedBefore (Integer filmId, Integer userId) {
        String sqlQuery = "select count(*) from likes where film_id = ? and user_id = ?";

        Integer result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId, userId);

        return result == 1;
    }

    public Film getFilm(int id) {
        String sqlQuery = "select film_id, name, description, duration, release_date, mpa_id " +
                "from films where film_id = ?";

        Film film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        film.setGenres(getGenres(id));
        film.setMpa(getRating(id));

        return film;
    }

    public Rating getRating(Integer filmId) {
        String sqlQuery = "select films.mpa_id, mpa_rating.mpa_name" +
                " from films " + " join mpa_rating on films.mpa_id = mpa_rating.mpa_id " + " where film_id = ? ";

        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToRating, filmId);
    }

    public List<Genre> getGenres(Integer id) {
        String sqlQuery = "select films_by_genre.genre_id, genres.name " +
                " from films_by_genre" + " join genres on films_by_genre.genre_id = genres.genre_id" + " where film_id = ?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre, id);
    }

    public List<Film> getAllFilms() {
        String sqlQuery = "select film_id, name, description, duration, release_date, mpa_id from films";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    public List<Genre> getAllGenres() {
        String sqlQuery = "select genre_id, name from genres";

        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }


    /*public int addNewFilm(Film film) {
        String sqlQuery = "insert into films (name, description, duration, release_date, mpa_id)" +
                "values (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getDuration(), film.getReleaseDate(),
                film.getMpa().getId());
        log.info("Film added");
        return 1;
    }*/

    public int addNewFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        int filmId = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();

        if ((film.getGenres() != null) && (!film.getGenres().isEmpty())) {
            updateGenresForFilm(film.getGenres(), filmId);
        }
        return filmId;
    }


    public Film updateFilm(Film film) {
        String sqlQuery = "update films set " + "name = ?, description = ?, duration = ?, release_date = ?,  " +
                "mpa_id = ?" + "where id = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getDuration()
                , film.getReleaseDate()
                , film.getMpa().getId()
                , film.getId());

        clearAllGenres(film.getId());

        if ((film.getGenres() != null) && (!film.getGenres().isEmpty())) {
            updateGenresForFilm(film.getGenres(), film.getId());
        }

        return getFilm(film.getId());
    }

    public boolean removeFilm(Integer id) {
        String sqlQuery = "delete from films where id = ?";

        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    public void putNewLike(Integer filmId, Integer userId) {
        String sqlQuery = "insert into likes (film_id, user_id)" +
                "values (?, ?)";

        jdbcTemplate.update(sqlQuery, filmId, userId);
        log.info("Like added");
    }

    public void removeLike(Integer filmId, Integer userId) {
        String sqlQuery = "delete from likes where film_id = ? and user_id = ?";

        jdbcTemplate.update(sqlQuery, filmId, userId);
        log.info("Like removed");
    }

    public List<Like> getLikes(Integer filmId) {
        String sqlQuery = "select user_id, film_id" + "from likes where film_id = ?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToLike, filmId);
    }

    public List<Rating> getAllRatings() {
        String sqlQuery = "select mpa_id, mpa_name" +
                "from mpa_rating";

        return jdbcTemplate.query(sqlQuery, this::mapRowToRating);
    }

    /*private void deleteRatingsForFilm(Integer id) {
        String sqlQuery = "delete from film_by_genre where id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }*/

    private void updateGenresForFilm(List<Genre> list, Integer FilmId) {
        for (Genre g : list) {
            int genreId = g.getId();
            String sqlQuery = "insert into films_by_genre (film_id, genre_id)" +
                    "values (?, ?)";

            jdbcTemplate.update(sqlQuery, FilmId, genreId);
            log.info("Genre added");
        }
    }

    private void clearAllGenres(Integer filmId) {
        String sqlQuery = "delete from films_by_genre where id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
        log.info("Genres cleared out");
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {

        // It is used to map each row of a ResultSet to an object.
        Film film = Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .releaseDate(resultSet.getDate("release_date"))
                .build();

        return film;
    }

    private Rating mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        // It is used to map each row of a ResultSet to an object.
        return Rating.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("mpa_name"))
                .build();
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        // It is used to map each row of a ResultSet to an object.
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("genre_name"))
                .build();
    }

    private Like mapRowToLike(ResultSet resultSet, int rowNum) throws SQLException {
        // It is used to map each row of a ResultSet to an object.
        return Like.builder()
                .filmId(resultSet.getInt("genre_id"))
                .userId(resultSet.getInt("user_id"))
                .build();
    }
}
