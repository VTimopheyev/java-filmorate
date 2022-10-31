package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

public interface FilmStorage {
    int addNewFilm(Film film);

    boolean filmExists(Integer id);

    Film updateFilm(Film film);

   // Map<Integer, Film> getFilms();

    List<Film> getAllFilms();

    void putNewLike(Integer filmId, Integer userId);

    void removeLike(Integer filmId, Integer userId);

    Film getFilm(int id);

    boolean likedBefore(Integer filmId, Integer userId);

    List<Like> getLikes(Integer filmId);

    Rating getRating(Integer id);

    List<Rating> getAllRatings();

    List<Genre> getGenres(Integer filmId);

    List<Genre> getAllGenres();

    boolean checkRating(Integer id);

    boolean checkGenreExist (Integer id);

    Genre getGenre(Integer id);
}
