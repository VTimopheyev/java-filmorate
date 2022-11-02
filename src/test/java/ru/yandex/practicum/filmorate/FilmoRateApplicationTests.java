package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;


import java.util.Date;
import java.util.Optional;



@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {


    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    User user;

    @BeforeEach
    public void createUser (){
        user = User.builder()
                .name("Norman")
                .email("user@mail.com")
                .login("somelogin")
                .birthday(new Date(1985, 04, 12))
                .build();
    }

    @Test
    public void testFindUserById() {
        userStorage.addNewUser(user);
        Optional <User> userOptional = Optional.ofNullable(userStorage.getUser(1));
        Assertions.assertNotNull(userOptional.get());
    }
}
