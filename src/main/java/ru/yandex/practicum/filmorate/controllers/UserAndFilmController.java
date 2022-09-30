package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@Component
public class UserAndFilmController {
    private UserService userService;
    private FilmService filmService;

    @Autowired
    public UserAndFilmController(UserService userService, FilmService filmService) throws RuntimeException {
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
        return userService.getUserById(id);
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

    @PostMapping("/films")
    public Film createNewFilm(@RequestBody Film film) {
        return filmService.addNewFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/films")
    public ArrayList<Film> getAllFilms() {
        return filmService.getAllFilmsList();
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable int id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public List<Integer> putLikeToFilm(@PathVariable int id, @PathVariable int userId) {
        if (filmService.checkFilmExist(id) && userService.checkUserExist(userId)) {
            return filmService.putLike(id, userId);
        }
        return null;
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public List<Integer> removeLike(@PathVariable int id, @PathVariable int userId) {
        if (filmService.checkFilmExist(id) && userService.checkUserExist(userId)) {
            return filmService.removeLike(id, userId);
        }
        return null;
    }

    @GetMapping("/films/popular")
    public List<Film> getMostLikedFilms(@RequestParam(required = false, defaultValue = "10") Long count) {
        return filmService.getMostLikedFilms(count);
    }
}

//PUT /users/{id}/friends/{friendId} — добавление в друзья.
//DELETE /users/{id}/friends/{friendId} — удаление из друзей.
//GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
//GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.электронная почта не может быть пустой и должна содержать символ @;


