package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Component
public class MpaDbStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Rating getRating(Integer id) {
        String sqlQuery = "select mpa_id, mpa_name " +
                "from mpa_rating where mpa_id = ?";

        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToRating, id);
    }

    public List<Rating> getAllRatings() {
        String sqlQuery = "select mpa_id, mpa_name" +
                " from mpa_rating";

        return jdbcTemplate.query(sqlQuery, this::mapRowToRating);
    }

    private Rating mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        // It is used to map each row of a ResultSet to an object.
        return Rating.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("mpa_name"))
                .build();
    }
}
