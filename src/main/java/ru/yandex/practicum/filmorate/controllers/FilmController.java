package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exceptions.InstanceNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
public class FilmController {




    @PostMapping
    public Film createNewFilm(@RequestBody Film film) throws ValidationException {

    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws Exception {

    }

    @GetMapping
    public ArrayList<Film> getAllFilms() {

    }

}

//название не может быть пустым;
//максимальная длина описания — 200 символов;
//дата релиза — не раньше 28 декабря 1895 года;
//продолжительность фильма должна быть положительной.
