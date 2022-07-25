package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.InstanceNotFoundException;
import ru.yandex.practicum.filmorate.service.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
public class FilmController {

    private HashMap<Integer, Film> films = new HashMap<>();
    private final LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private int count = 0;

    @PostMapping
    public Film createNewFilm(@RequestBody Film film) throws ValidationException {
        if (validateFilm(film)) {
            count++;
            film.setId(count);
            films.put(count, film);
            log.info("New Film created successfully");
        }
        return films.get(film.getId());
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws Exception {
        if (validateFilm(film) && checkFilmExist(film)) {
            films.put(film.getId(), film);
            log.info("Film updated successfully");
        }
        return films.get(film.getId());
    }

    @GetMapping
    public ArrayList<Film> getAllFilms() {
        ArrayList<Film> list = new ArrayList<>();
        for (int id : films.keySet()) {
            list.add(films.get(id));
        }
        log.info("Films list has been sent");
        return list;
    }

    private boolean validateFilm(Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
            log.info("Film name must not be empty");
            throw new ValidationException("Film name must not be empty");

        } else if (film.getDescription().length() > 200) {
            log.info("Description is too long");
            throw new ValidationException("Description is too long");

        } else if (film.getReleaseDate().isBefore(earliestReleaseDate)) {
            log.info("Release date must not be earlier, than 28-12-1895");
            throw new ValidationException("Release date must not be earlier, than 28-12-1895");

        } else if (film.getDuration() < 0) {
            log.info("Duration must not be negative");
            throw new ValidationException("Duration must not be negative");

        }
        return true;
    }

    private boolean checkFilmExist(Film film) throws InstanceNotFoundException {
        if (films.containsKey(film.getId())) {
            return true;
        } else {
            log.info("No such film to update");
            throw new InstanceNotFoundException("No such film to update");
        }
    }
}

//название не может быть пустым;
//максимальная длина описания — 200 символов;
//дата релиза — не раньше 28 декабря 1895 года;
//продолжительность фильма должна быть положительной.
