package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InstanceNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.sql.Date.valueOf;

@Service
public class FilmService {

    private final static Logger log = LoggerFactory.getLogger(FilmService.class);
    private final Date earliestReleaseDate = valueOf("1895-12-28");
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addNewFilm(Film film) {
        if (!filmStorage.filmExists(film.getId())) {
            int filmId = filmStorage.addNewFilm(film);
            return filmStorage.getFilm (filmId);
        } else {
            throw new ValidationException("Wrong film credentials");
        }
    }

    public Film updateFilm(Film film) {
        if (validateFilm(film) && checkFilmExist(film.getId())) {
            return filmStorage.updateFilm(film);
        } else {
            throw new ValidationException("Wrong film credentials");
        }
    }

    public List<Film> getAllFilmsList() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        if (checkFilmExist(id)) {
            return filmStorage.getFilm(id);
        } else {
            throw new InstanceNotFoundException("There is no such film");
        }
    }

    public List<Like> putLike(Integer filmId, Integer userId) {
        if (!filmStorage.likedBefore(filmId, userId)) {
            filmStorage.putNewLike(filmId, userId);
            return filmStorage.getLikes(filmId);
        } else {
            throw new InstanceNotFoundException("Cannot be liked");
        }
    }

    public List<Like> removeLike(Integer filmId, Integer userId) {
        if (checkIfFilmWasPreviouslyLiked(filmId, userId)) {
            filmStorage.removeLike(filmId, userId);
            return filmStorage.getLikes(filmId);
        } else {
            throw new ValidationException("The film was not liked");
        }
    }

    public List<Film> getMostLikedFilms(Long limit) {

        List<Film> allFilms = filmStorage.getAllFilms();

        if (allFilms.isEmpty()) {
            throw new InstanceNotFoundException("There is no liked films");
        }

        for (Film f : allFilms) {
            f.setLikes(filmStorage.getLikes(f.getId()));
        }

        List<Film> mostLikedFilms = allFilms.stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(limit)
                .collect(Collectors.toList());

        return mostLikedFilms;
    }

    public Rating getFilmRating(Integer filmId) {
        if (checkFilmExist(filmId)) {
            return filmStorage.getRating(filmId);
        } else {
            throw new InstanceNotFoundException("There is no such film");
        }
    }

    public List<Rating> getAllRatings() {
        return filmStorage.getAllRatings();
    }

    public List<Genre> getFilmGenres(Integer filmId) {
        if (checkFilmExist(filmId)) {
            return filmStorage.getGenres(filmId);
        } else {
            throw new InstanceNotFoundException("There is no such film");
        }
    }

    public List<Genre> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    private boolean validateFilm(Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
            log.info("Film name must not be empty");
            throw new ValidationException("Film name must not be empty");

        } else if (film.getDescription().length() > 200) {
            log.info("Description is too long");
            throw new ValidationException("Description is too long");

        } else if (film.getReleaseDate().before(earliestReleaseDate)) {
            log.info("Release date must not be earlier, than 28-12-1895");
            throw new ValidationException("Release date must not be earlier, than 28-12-1895");

        } else if (film.getDuration() < 0) {
            log.info("Duration must not be negative");
            throw new ValidationException("Duration must not be negative");

        } else if (film.getMpa() == null) {
            log.info("Rating must not be empty");
            throw new ValidationException("Rating must not be empty");

        }
        return true;
    }

    public boolean checkFilmExist(int id) throws InstanceNotFoundException {
        if (filmStorage.filmExists(id)) {
            return true;
        } else {
            log.info("No such film to update");
            throw new InstanceNotFoundException("No such film to update");
        }
    }

    public boolean checkIfFilmWasPreviouslyLiked(Integer filmId, Integer userId) {
        return filmStorage.likedBefore (filmId, userId);
    }
}
