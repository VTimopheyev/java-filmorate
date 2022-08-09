package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface UserStorage {
    User addNewUser(User user);
    User updateUser (User user);
    HashMap<Integer, User> getUsers();
    List<User> sendAllUsersList();
    void addNewFriendToUser(Integer id, Integer friendId);
    void removeUserFromFriends(Integer id, Integer friendId);
    List<User> getCommonFriendsList(Integer id, Integer friendId);
}
