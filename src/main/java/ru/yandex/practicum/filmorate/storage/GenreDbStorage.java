package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Component
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getAllGenres() {
        String sqlQuery = "select genre_id, name from genres";

        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    public Genre getGenre(Integer id) {
        String sqlQuery = "select genre_id, name from genres" + " where genre_id = ?";

        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        // It is used to map each row of a ResultSet to an object.
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
