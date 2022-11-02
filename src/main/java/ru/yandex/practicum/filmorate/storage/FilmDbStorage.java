package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FilmDbStorage implements FilmStorage {

    private final static Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDbStorage;


    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaDbStorage mpaDbStorage, LikeDbStorage likeDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
    }

    public boolean filmExists(Integer id) {

        String sqlQuery = "select count(*) from films where film_id = ?";
        Integer result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);

        return result == 1;
    }

    public boolean likedBefore(Integer filmId, Integer userId) {
        String sqlQuery = "select count(*) from likes where film_id = ? and user_id = ?";

        Integer result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId, userId);

        return result == 1;
    }

    public Film getFilm(int id) {
        String sqlQuery = "select film_id, name, description, duration, release_date " +
                "from films where film_id = ?";
        Film film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);

        sqlQuery = "select mpa_id " +
                "from films where film_id = ?";
        Integer ratingId = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);

        film.setGenres(getGenres(id));
        film.setMpa(mpaDbStorage.getRating(ratingId));

        return film;
    }

    public boolean checkRating(Integer id) {
        String sqlQuery = "select count(*) from mpa_rating where mpa_id = ?";
        Integer result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);

        return result == 1;
    }

    public List<Genre> getGenres(Integer id) {
        String sqlQuery = "select films_by_genre.genre_id, genres.name " +
                " from films_by_genre" + " join genres on films_by_genre.genre_id = genres.genre_id" + " where film_id = ?";

        List <Genre> genres = jdbcTemplate.query(sqlQuery, this::mapRowToGenre, id);
        List <Genre> uniqueGenres = new ArrayList<>();
        for (Genre g : genres){
            if (!uniqueGenres.contains(g)){
                uniqueGenres.add(g);
            }
        }
        return uniqueGenres;
    }

    public List<Film> getAllFilms() {
        String sqlQuery = "select film_id, name, description, duration, release_date, mpa_id from films";

        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);

        for (Film f : films) {
            f.setGenres(getGenres(f.getId()));

            sqlQuery = "select mpa_id " +
                    "from films where film_id = ?";
            Integer ratingId = jdbcTemplate.queryForObject(sqlQuery, Integer.class, f.getId());
            f.setMpa(mpaDbStorage.getRating(ratingId));
        }

        return films;
    }

    public boolean checkGenreExist(Integer id) {
        String sqlQuery = "select count(*) from genres where genre_id = ?";
        Integer result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);

        return result == 1;
    }

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
        String sqlQuery = "update films set " + "name = ?, description = ?, duration = ?, release_date = ?, mpa_id = ?"
                + " where film_id = ?";
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
        String sqlQuery = "delete from films where film_id = ?";

        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    public List<Like> getLikes(Integer filmId) {
        String sqlQuery = "select user_id, film_id" + " from likes where film_id = ?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToLike, filmId);
    }

    private void updateGenresForFilm(List<Genre> list, Integer FilmId) {

        for (Genre g : list) {
            int genreId = g.getId();
            String sqlQuery = "insert into films_by_genre (film_id, genre_id) " +
                    " values (?, ?)";

            jdbcTemplate.update(sqlQuery, FilmId, genreId);
            log.info("Genre added");
        }
    }

    private void clearAllGenres(Integer filmId) {
        String sqlQuery = "delete from films_by_genre where film_id = ?";
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

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        // It is used to map each row of a ResultSet to an object.
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    private Like mapRowToLike(ResultSet resultSet, int rowNum) throws SQLException {
        // It is used to map each row of a ResultSet to an object.
        return Like.builder()
                .filmId(resultSet.getInt("film_id"))
                .userId(resultSet.getInt("user_id"))
                .build();
    }
}
