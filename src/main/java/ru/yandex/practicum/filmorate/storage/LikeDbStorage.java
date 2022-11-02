package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.InstanceNotFoundException;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;
@Component
public class LikeDbStorage {

    private final JdbcTemplate jdbcTemplate;
    private final static Logger log = LoggerFactory.getLogger(LikeDbStorage.class);

    @Autowired
    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void putNewLike(Integer filmId, Integer userId) {
        String sqlQuery = "insert into likes (film_id, user_id)" +
                " values (?, ?)";

        jdbcTemplate.update(sqlQuery, filmId, userId);
        log.info("Like added");
    }

    public void removeLike(Integer filmId, Integer userId) {
        String sqlQuery = "delete from likes where film_id = ? and user_id = ?";

        jdbcTemplate.update(sqlQuery, filmId, userId);
        log.info("Like removed");
    }

}
