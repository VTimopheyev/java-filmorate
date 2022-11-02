package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTests {


    private final UserDbStorage userStorage;
    User user1;
    User user2;
    User user3;


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
    public void testFindUserById() {
        createUsers ();
        userStorage.addNewUser(user1);
        Optional<User> userOptional = Optional.ofNullable(userStorage.getUser(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void updateUser(){
        createUsers();
        User updatedUser = User.builder()
                .id(1)
                .name("Joshua")
                .email("updated@mail.com")
                .login("somelogin3")
                .birthday(new Date(1985, 04, 12))
                .build();
        userStorage.addNewUser(user1);
        userStorage.updateUser(updatedUser);

        Optional <User> userOptional = Optional.ofNullable(userStorage.getUser(1));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "Joshua")
                );
    }

    @Test
    public void getUsers (){
        createUsers();
        userStorage.addNewUser(user1);
        userStorage.addNewUser(user2);
        userStorage.addNewUser(user3);

        List<User> users = userStorage.getAllUsers();
        Assertions.assertEquals(5, users.size());

    }

    @Test
    public void checkUserExistTest(){
        System.out.println(userStorage.getAllUsers());
        Assertions.assertTrue(userStorage.checkUserExist(2));
    }
}