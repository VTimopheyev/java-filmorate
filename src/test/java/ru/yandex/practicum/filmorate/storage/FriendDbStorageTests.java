package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Date;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendDbStorageTests {
    private final UserDbStorage userStorage;
    private final FriendDbStorage friendDbStorage;
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
    public void addAndGetFriendTest(){
        createUsers();
        userStorage.addNewUser(user1);
        userStorage.addNewUser(user2);
        friendDbStorage.addFriend(1, 2);
        Assertions.assertEquals(1, friendDbStorage.getAllFriendsOfUser(1).size());
    }

    @Test
    public void removeFriendTests(){
        friendDbStorage.removeUserFromFriends(1, 2);
        Assertions.assertTrue(friendDbStorage.getAllFriendsOfUser(1).isEmpty());
    }




}
