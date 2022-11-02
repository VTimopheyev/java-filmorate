package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTests {

    private final FilmDbStorage filmDbStorage;
    Film film1;
    Film film2;
    Film film3;

    Rating rating1;
    Rating rating2;

    public void createFilms() {
        rating1 = Rating.builder()
                .id(1)
                .build();

        rating2 = Rating.builder()
                .id(3)
                .build();

        film1 = Film.builder()
                .name("Mask")
                .duration(120)
                .description("Some movie description")
                .releaseDate(new Date(1985, 04, 12))
                .mpa(rating1)
                .build();

        film2 = Film.builder()
                .name("Lost")
                .duration(100)
                .description("Some more movie description")
                .releaseDate(new Date(1980, 03, 03))
                .mpa(rating2)
                .build();

        film3 = Film.builder()
                .name("Friends")
                .duration(105)
                .description("Some awesome movie description")
                .releaseDate(new Date(1977, 12, 31))
                .mpa(rating1)
                .build();
    }

    @Test
    public void addNewFilmAndGetTest() {
        System.out.println(filmDbStorage.getAllFilms());
        createFilms();
        filmDbStorage.addNewFilm(film3);
        Optional<Film> userOptional = Optional.ofNullable(filmDbStorage.getFilm(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
        System.out.println(filmDbStorage.getAllFilms());
    }

    @Test
    public void filmExiststest (){
        createFilms();
        System.out.println(filmDbStorage.getAllFilms());
        filmDbStorage.addNewFilm(film1);
        Assertions.assertTrue(filmDbStorage.filmExists(2));
        System.out.println(filmDbStorage.getAllFilms());
    }

    @Test
    public void getAllFilms (){
        System.out.println(filmDbStorage.getAllFilms());
        createFilms();
        Assertions.assertEquals(1, filmDbStorage.getAllFilms().size());
        filmDbStorage.addNewFilm(film1);
        filmDbStorage.addNewFilm(film2);
        Assertions.assertEquals(3, filmDbStorage.getAllFilms().size());
        System.out.println(filmDbStorage.getAllFilms());
    }

    @Test
    public void filmUpdateTest() {
        System.out.println(filmDbStorage.getAllFilms());
        createFilms();
        filmDbStorage.addNewFilm(film2);
        Film updatedFilm = Film.builder()
                .id(1)
                .name("Updated")
                .duration(180)
                .description("Some updated movie description")
                .releaseDate(new Date(1901, 11, 03))
                .mpa(rating1)
                .build();

        filmDbStorage.updateFilm(updatedFilm);
        Optional<Film> userOptional = Optional.ofNullable(filmDbStorage.getFilm(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Updated")
                );
        System.out.println(filmDbStorage.getAllFilms());
    }
}
