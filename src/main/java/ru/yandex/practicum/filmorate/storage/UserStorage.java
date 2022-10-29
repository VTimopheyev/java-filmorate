package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface UserStorage {
    boolean checkFriendshipExist(Integer userId, Integer friendId);

    boolean checkFriendshipRequestedByUser(Integer userId, Integer friendId);

    void removeFriendshipRequest(Integer userId, Integer friendId);

    int addNewUser(User user);
    User updateUser (User user);

    void addFriend(Integer userId, Integer friendId);

    List<User> getAllFriendsOfUser(Integer userId);

    void removeUserFromFriends(Integer id, Integer friendId);

    User getUser(int addNewUser);

    List<User> getAllUsers();

    boolean checkUserExist(Integer id);

    boolean checkIfUserInFriendsList(Integer userId, Integer friendId);

    void approveFriendShip(Integer userId, Integer friendId);

    User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException;
}
