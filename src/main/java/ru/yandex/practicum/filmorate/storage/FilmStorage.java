package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import java.util.List;

public interface FilmStorage {
    int addNewFilm(Film film);

    boolean filmExists(Integer id);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Film getFilm(int id);

    boolean likedBefore(Integer filmId, Integer userId);

    List<Like> getLikes(Integer filmId);

    List<Genre> getGenres(Integer filmId);

    boolean checkRating(Integer id);

    boolean checkGenreExist (Integer id);
}
