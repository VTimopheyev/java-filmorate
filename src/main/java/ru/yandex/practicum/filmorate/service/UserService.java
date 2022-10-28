package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InstanceNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    private  UserStorage userStorage;
    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public boolean checkUserExist(Integer userId) {
        return userStorage.checkUserExist(userId);
    }

    public User getUser (Integer userId){
        if (checkUserExist(userId)){
            return userStorage.getUser(userId);
        }
        else {
            throw new InstanceNotFoundException("No such user");
        }
    }

    public User addNewUser(User user) throws ValidationException {
        if (validateUser(user)) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            return userStorage.getUser(userStorage.addNewUser(user));
        } else {
            throw new ValidationException("Wrong User credentials");
        }
    }

    public User updateUser(User user) {
        if (validateUser(user) && checkUserExist(user.getId())) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }

            return userStorage.updateUser(user);
        } else {
            throw new ValidationException("Wrong User credentials");
        }
    }

    public List<User> getAllUsersList() {
        return userStorage.getAllUsers();
    }

    public void addNewFriendToUser(Integer userId, Integer friendId) {
        if (!checkUserExist(userId) || (!checkUserExist(friendId))) {
            throw new InstanceNotFoundException("No such user or friend");
        }
        if (userStorage.checkFriendshipExist(userId, friendId) || userStorage.checkFriendshipRequestedByUser(userId, friendId)) {
            throw new ValidationException("Friendship already requested or confirmed");
        }
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        if (!checkUserExist(userId) || (!checkUserExist(friendId))) {
            throw new InstanceNotFoundException("No such user or friend");
        }
        if (!userStorage.checkFriendshipExist(userId, friendId) ||
                !userStorage.checkFriendshipRequestedByUser(userId, friendId)) {
            throw new ValidationException("Friendship hasn`t been requested or confirmed");
        }
        userStorage.removeUserFromFriends(userId, friendId);
    }

    public List<User> getAllFriends(int id) throws InstanceNotFoundException {
        if (!checkUserExist(id)) {
            throw new InstanceNotFoundException("No such user");
        }
        return userStorage.getAllFriendsOfUser(id);
    }

    public List<User> getCommonFriendsList(Integer userid, Integer friendId) {

        List<User> usersFriends = getAllFriends(userid);
        List<User> friendsFriends = getAllFriends(friendId);
        List<User> commonFriends = new ArrayList<>();
        for (User u : usersFriends) {
            if (friendsFriends.contains(u)) {
                commonFriends.add(u);
            }
        }

        return commonFriends;
    }

    private boolean validateUser(User user) throws ValidationException {
        boolean validated = true;

        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Email is wrong");

        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Login is wrong");

        } else if (user.getBirthday().toInstant().isAfter(Instant.now())) {
            throw new ValidationException("Birth date must not be in future");

        }
        return validated;
    }

    public void removeUserFromFriends(int id, int friendId) {
        if (checkUserExist(id) && checkUserExist(friendId)) {
            userStorage.removeUserFromFriends(id, friendId);
        } else {
            throw new InstanceNotFoundException("No such user or friend exists");
        }
    }
}
