package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.ArrayList;
import java.util.HashMap;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private HashMap<Integer, Film> films = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private int count = 0;

    public HashMap<Integer, Film> getFilms() {
        return films;
    }



    public Film addNewFilm(Film film) {
        count++;
        film.setId(count);
        films.put(count, film);
        log.info("New Film created successfully");
        return films.get(film.getId());
    }

    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        log.info("Film was updated successfully");
        return films.get(film.getId());
    }

    public ArrayList <Film> getAllFilms(){
        ArrayList<Film> list = new ArrayList<>();
        for (int id : films.keySet()) {
            list.add(films.get(id));
        }
        log.info("Films list has been sent");
        return list;
    }

    public void putNewLike(Integer filmId, Integer userId){
        films.get(filmId).getLikesList().add(userId);
        log.info("Like added");
    }

    public void removeLike(Integer filmId, Integer userId){
        films.get(filmId).getLikesList().remove(userId);
        log.info("Like removed");
    }
}
