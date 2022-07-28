package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.InstanceNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    UserStorage userStorage;
    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addNewUser(User user) throws ValidationException {
        if (validateUser(user)) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            return userStorage.addNewUser(user);
        }
        return null;
    }

    public User updateUser(User user) {
        if (validateUser(user) && checkUserExist(user)) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            return userStorage.updateUser(user);
        }
        return null;
    }

    public ArrayList<User> getAllUsersList() {

        return userStorage.sendAllUsersList();
    }

    public void addNewFriendToUser(Integer id, Integer friendId) {
        if (checkUserExist(userStorage.getUsers().get(id)) && checkUserExist(userStorage.getUsers().get(id))) {
            userStorage.addNewFriendToUser(id, friendId);
        }
    }

    public void removeUserFromFriends(Integer id, Integer friendId) {
        if (checkUserExist(userStorage.getUsers().get(id)) &&
                checkUserExist(userStorage.getUsers().get(friendId)) &&
                checkUserInFriendsList(id, friendId)) {
            userStorage.removeUserFromFriends (id, friendId);
        }
    }

    public List <User> getCommonFriendsList (Integer id, Integer friendId){
        if (checkUserExist(userStorage.getUsers().get(id)) &&
                checkUserExist(userStorage.getUsers().get(friendId))){
            return userStorage.getCommonFriendsList(id, friendId);
        }
        return null;
    }

    private boolean validateUser(User user) throws ValidationException {
        boolean validated = true;

        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Email is wrong");

        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Login is wrong");

        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Birth date must not be in future");

        }
        return validated;
    }

    private boolean checkUserExist(User user) throws InstanceNotFoundException {
        if (userStorage.getUsers().containsKey(user.getId())) {
            return true;
        } else {
            log.info("No such user to update");
            throw new InstanceNotFoundException("No such user to update");
        }
    }

    private boolean checkUserInFriendsList(Integer id, Integer friendId) throws InstanceNotFoundException {
        if (userStorage.getUsers().get(id).getFriends().contains(friendId)) {
            return false;
        }else {
            log.info("There is a friend with this ID in the list already");
            throw new InstanceNotFoundException("There is a friend with this ID in the list already");
        }
    }
}
