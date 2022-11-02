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
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Date;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikeDbStorageTests {

    private final LikeDbStorage likeDbStorage;
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    User user1;
    User user2;
    User user3;

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

    public void createUsers (){
        user1 = User.builder()
                .name("Joe")
                .email("user1@mail.com")
                .login("somelogin1")
                .birthday(new Date(1985, 04, 12))
                .build();

        user2 = User.builder()
                .name("John")
                .email("user2@mail.com")
                .login("somelogin2")
                .birthday(new Date(1985, 04, 12))
                .build();

        user3 = User.builder()
                .name("Jake")
                .email("user3@mail.com")
                .login("somelogin3")
                .birthday(new Date(1985, 04, 12))
                .build();
    }

    @Test
    public void addNewLikeTest (){

        createUsers();
        createFilms();

        filmDbStorage.addNewFilm(film1);
        filmDbStorage.addNewFilm(film2);
        userDbStorage.addNewUser(user1);
        userDbStorage.addNewUser(user2);
        userDbStorage.addNewUser(user3);

        likeDbStorage.putNewLike(1, 2);
        Assertions.assertEquals(1, filmDbStorage.getLikes(1).size());
        likeDbStorage.putNewLike(1, 3);
        Assertions.assertEquals(2, filmDbStorage.getLikes(1).size());
        Assertions.assertEquals(0, filmDbStorage.getLikes(2).size());
    }

    @Test
    public void likeRemoveTest (){
        likeDbStorage.removeLike(1, 2);
        Assertions.assertEquals(1, filmDbStorage.getLikes(1).size());
    }
}
