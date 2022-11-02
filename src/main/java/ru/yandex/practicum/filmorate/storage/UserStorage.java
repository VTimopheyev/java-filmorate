package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface UserStorage {
    int addNewUser(User user);
    User updateUser (User user);

    User getUser(int addNewUser);

    List<User> getAllUsers();

    boolean checkUserExist(Integer id);

    User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException;
}
