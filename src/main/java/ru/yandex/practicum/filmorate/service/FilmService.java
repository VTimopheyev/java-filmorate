package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InstanceNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final static Logger log = LoggerFactory.getLogger(FilmService.class);
    private final LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {

        this.filmStorage = filmStorage;
    }

    public Film addNewFilm(Film film) {
        if (validateFilm(film)) {
            if (film.getLikes() == null){
                film.setLikes(new ArrayList<>());
            }
            return filmStorage.addNewFilm(film);
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

    public ArrayList<Film> getAllFilmsList() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        if (checkFilmExist(id)) {
            return filmStorage.updateFilm(filmStorage.getFilms().get(id));
        } else {
            throw new InstanceNotFoundException("There is no such film");
        }
    }

    public List<Integer> putLike(Integer filmId, Integer userId) {
        if (!checkIfFilmWasPreviouslyLiked(filmId, userId)) {
            filmStorage.putNewLike(filmId, userId);
            return filmStorage.getFilms().get(filmId).getLikes();
        } else {
            throw new InstanceNotFoundException("Cannot be liked");
        }
    }

    public List<Integer> removeLike(Integer filmId, Integer userId) {
        if (checkFilmExist(filmId) && checkIfFilmWasPreviouslyLiked(filmId, userId)) {
            filmStorage.removeLike(filmId, userId);
            return filmStorage.getFilms().get(filmId).getLikes();
        } else {
            throw new ValidationException("The like cannot be removed");
        }
    }

    public List<Film> getMostLikedFilms(Long limit) {
        List<Film> allFilms = new ArrayList<>();
        for (int key : filmStorage.getFilms().keySet()) {
            allFilms.add(filmStorage.getFilms().get(key));
        }
        if (allFilms.isEmpty()) {
            throw new InstanceNotFoundException("There is no liked films");
        }
        List <Film> mostLikedFilms = allFilms.stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(limit)
                .collect(Collectors.toList());

        return mostLikedFilms;
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

    public boolean checkFilmExist(int id) throws InstanceNotFoundException {
        if (filmStorage.getFilms().containsKey(id)) {
            return true;
        } else {
            log.info("No such film to update");
            throw new InstanceNotFoundException("No such film to update");
        }
    }


    public boolean checkIfFilmWasPreviouslyLiked (Integer filmId, Integer userId){
        if (filmStorage.getFilms().get(filmId).getLikes().contains(userId)){
            return true;
        } else {
            return false;
        }
    }
}
