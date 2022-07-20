package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
public class FilmController {

    HashMap<Integer, Film> films = new HashMap<>();
    private final LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private int count = 0;

    @PostMapping
    public Film createNewFilm(@RequestBody Film film) {
        try {
            if (validateFilm(film)) {
                count++;
                film.setId(count);
                films.put(count, film);
                log.debug("New Film created successfully");
            }

        } catch (ValidationException e) {
            log.info(e.getMessage());
        }
        return films.get(film.getId());
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        try {
            if (validateFilm(film) && checkFilmExist(film)) {
                films.put(film.getId(), film);
                log.debug("Film updated successfully");
            }
        } catch (ValidationException e) {
            log.info(e.getMessage());
        }
        return films.get(film.getId());
    }

    @GetMapping
    public ArrayList<Film> getAllFilms() {
        ArrayList<Film> list = new ArrayList<>();
        for (int id : films.keySet()){
            list.add(films.get(id));
        }
        log.info("Films list has been sent");
        return list;
    }

    public boolean validateFilm(Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
            System.out.println(film.getName().isEmpty() + "1");
            throw new ValidationException("Film name must not be empty");

        } else if (film.getDescription().length() > 200) {
            System.out.println((film.getDescription().length() > 200) + "2");
            throw new ValidationException("Description is too long");

        } else if (film.getReleaseDate().isBefore(earliestReleaseDate)) {
            System.out.println(film.getReleaseDate().isBefore(earliestReleaseDate) + "3");
            throw new ValidationException("Release date must not be earlier, than 28-12-1895");

        } else if (film.getDuration() < 0) {
            System.out.println((film.getDuration() < 0) + "4");
            throw new ValidationException("Duration must not be negative");

        }
        return true;
    }

    public boolean checkFilmExist(Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            return true;
        } else {
            throw new ValidationException("No such film to update");
        }
    }
}

//название не может быть пустым;
//максимальная длина описания — 200 символов;
//дата релиза — не раньше 28 декабря 1895 года;
//продолжительность фильма должна быть положительной.
