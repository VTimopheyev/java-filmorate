package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface FilmStorage {
    Film addNewFilm(Film film);

    Film updateFilm(Film film);

    Map<Integer, Film> getFilms();

    ArrayList<Film> getAllFilms();

    void putNewLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);
}
