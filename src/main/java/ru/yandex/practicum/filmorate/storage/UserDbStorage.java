package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserDbStorage implements UserStorage {

    private final static Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean checkUserExist(Integer id) {
        String sqlQuery = "select count(*) from users where user_id = ?";
        Integer result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);
        return result == 1;
    }

    @Override
    public int addNewUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        return simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
    }

    @Override
    public User getUser(int id) {
        String sqlQuery = "select user_id, name, email, login, birthdate " +
                "from users where user_id = ?";

        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "update users set " + "name = ?, email = ?, login = ?, birthdate = ?" + " where user_id = ?";
        jdbcTemplate.update(sqlQuery
                , user.getName()
                , user.getEmail()
                , user.getLogin()
                , user.getBirthday()
                , user.getId());

        return getUser(user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "select user_id, name, email, login, birthdate from users";

        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {

        // It is used to map each row of a ResultSet to an object.
        return User.builder()
                .id(resultSet.getInt("user_id"))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .birthday(resultSet.getDate("birthdate"))
                .build();
    }
}
