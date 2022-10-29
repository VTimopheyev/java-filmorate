package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.*;

import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FilmController {
    private UserService userService;
    private FilmService filmService;

    @Autowired
    public FilmController(UserService userService, FilmService filmService) throws RuntimeException {
        this.userService = userService;
        this.filmService = filmService;
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
    public List<Film> getAllFilms() {
        return filmService.getAllFilmsList();
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable int id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public List<Like> putLikeToFilm(@PathVariable int id, @PathVariable int userId) {
        if (filmService.checkFilmExist(id) && userService.checkUserExist(userId)) {
            return filmService.putLike(id, userId);
        }
        return null;
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public List<Like> removeLike(@PathVariable int id, @PathVariable int userId) {
        if (filmService.checkFilmExist(id) && userService.checkUserExist(userId)) {
            return filmService.removeLike(id, userId);
        }
        return null;
    }

    @GetMapping("/films/popular")
    public List<Film> getMostLikedFilms(@RequestParam(required = false, defaultValue = "10") Long count) {
        return filmService.getMostLikedFilms(count);
    }

    @GetMapping("/mpa/{id}")
    public Rating getFilmRating(@PathVariable int id) {
        return filmService.getFilmRating(id);
    }

    @GetMapping("/mpa")
    public List <Rating> getAllRatings() {
        return filmService.getAllRatings();
    }

    @GetMapping("/genres/{id}")
    public List <Genre> getFilmGenres(@PathVariable int id) {
        return filmService.getFilmGenres(id);
    }

    @GetMapping("/genres")
    public List <Genre> getAllGenres() {
        return filmService.getAllGenres();
    }
}

//PUT /users/{id}/friends/{friendId} — добавление в друзья.
//DELETE /users/{id}/friends/{friendId} — удаление из друзей.
//GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
//GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.электронная почта не может быть пустой и должна содержать символ @;


