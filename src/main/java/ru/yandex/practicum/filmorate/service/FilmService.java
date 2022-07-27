package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.InstanceNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FilmService {

    private final static Logger log = LoggerFactory.getLogger(FilmService.class);
    private final LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);
    FilmStorage filmStorage;

    @Autowired
    public FilmService (FilmStorage filmStorage){

        this.filmStorage = filmStorage;
    }

    public Film addNewFilm (Film film) {
        if (validateFilm(film)) {
            return filmStorage.addNewFilm(film);
        }
        return null;
    }

    public Film updateFilm (Film film) {
        if (validateFilm(film) && checkFilmExist(film)) {
            return filmStorage.updateFilm(film);

        }
        return null;
    }

    public List<Film> getAllFilmsList (){
        return filmStorage.getAllFilms();
    }

    public void putLike (Integer filmId, Integer userId){
        if (checkFilmExist(filmStorage.getFilms().get(filmId)) && !checkIfFilmWasPreviouslyLiked(filmId, userId)){
            filmStorage.putNewLike(filmId, userId);
        }
    }

    public void removeLike (Integer filmId, Integer userId){
        if (checkFilmExist(filmStorage.getFilms().get(filmId)) && checkIfFilmWasPreviouslyLiked(filmId, userId)){
            filmStorage.removeLike(filmId, userId);
        }
    }

    public List <Film> getMostLikedFilms (Long limit){
        List<Film> allFilms = new ArrayList<>();
        for (int key : filmStorage.getFilms().keySet()){
            allFilms.add(filmStorage.getFilms().get(key));
        }

        List <Film> mostLikedFilms = allFilms.stream()
                .sorted((o1, o2) -> o1.getLikesList().size() - o2.getLikesList().size())
                .limit(limit)
                .collect(Collectors.toList());

                return  mostLikedFilms;
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
        if (filmStorage.getFilms().containsKey(film.getId())) {
            return true;
        } else {
            log.info("No such film to update");
            throw new InstanceNotFoundException("No such film to update");
        }
    }

    private boolean checkIfFilmWasPreviouslyLiked (Integer filmId, Integer userId){
        if (filmStorage.getFilms().get(filmId).getLikesList().contains(userId)){
            return true;
        }
        else {
            log.info("The film was already liked by this user");
            throw new InstanceNotFoundException("The film was already liked by this user");
        }
    }
}
