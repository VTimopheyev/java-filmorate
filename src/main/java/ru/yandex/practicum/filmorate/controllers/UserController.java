package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
public class UserController {
    private UserService userService;
    private FilmService filmService;

    @Autowired
    public UserController(UserService userService, FilmService filmService) throws RuntimeException {
        this.userService = userService;
        this.filmService = filmService;
    }
    @PostMapping("/users")
    public User createNewUser(@RequestBody User user) {
        return userService.addNewUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/users")
    public List<User> getAllUsersList() {
        return userService.getAllUsersList();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUser(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) throws RuntimeException {
        userService.addNewFriendToUser(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable int id, @PathVariable int friendId) {
        userService.removeUserFromFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriendsList(@PathVariable int id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriendsList(id, otherId);
    }
}
