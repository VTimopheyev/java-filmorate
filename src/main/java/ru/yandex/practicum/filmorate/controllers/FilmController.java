package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.ValidationException;

import java.time.LocalDate;
import java.util.HashMap;

@RequestMapping("/films")
public class FilmController {

    HashMap<Integer, Film> films = new HashMap<>();
    private final LocalDate earliestReleaseDate = LocalDate.ofEpochDay(1895 - 12 - 28);
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    @PostMapping
    public Film createNewFilm(@RequestBody Film film) throws Exception {
        if (film.getName().isEmpty()) {
            Exception e = new ValidationException("Film name must not be empty");
            log.debug(e.getMessage());
            throw e;
        } else if (film.getDescription().length() > 200) {
            Exception e = new ValidationException("Description is too long");
            log.debug(e.getMessage());
            throw e;
        } else if (film.getReleaseDate().isBefore(earliestReleaseDate)) {
            Exception e = new ValidationException("Release date must not be earlier, than 28-12-1895");
            log.debug(e.getMessage());
            throw e;
        } else if (film.getDuration().isNegative()) {
            Exception e = new ValidationException("Duration must not be negative");
            log.debug(e.getMessage());
            throw e;
        }

        films.put(film.getId(), film);
        log.debug("New Film created successfully");
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws Exception {
        if (film.getName().isEmpty()) {
            Exception e = new ValidationException("Film name must not be empty");
            log.debug(e.getMessage());
            throw e;
        } else if (film.getDescription().length() > 200) {
            Exception e = new ValidationException("Description is too long");
            log.debug(e.getMessage());
            throw e;
        } else if (film.getReleaseDate().isBefore(earliestReleaseDate)) {
            Exception e = new ValidationException("Release date must not be earlier, than 28-12-1895");
            log.debug(e.getMessage());
            throw e;
        } else if (film.getDuration().isNegative()) {
            Exception e = new ValidationException("Duration must not be negative");
            log.debug(e.getMessage());
            throw e;
        }

        films.put(film.getId(), film);
        log.debug("Film updated successfully");
        return film;
    }

    @GetMapping
    public HashMap<Integer, Film> getAllFilms() {
        log.info("Films list has been sent");
        return films;
    }
}

//название не может быть пустым;
//максимальная длина описания — 200 символов;
//дата релиза — не раньше 28 декабря 1895 года;
//продолжительность фильма должна быть положительной.
