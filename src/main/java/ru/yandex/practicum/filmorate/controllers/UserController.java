package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exceptions.InstanceNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {


    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController (UserService userService){
        this.userService = userService;
    }


    @PostMapping
    public User createNewUser(@RequestBody User user) throws ValidationException {

        log.info("New user created successfully");

        return users.get(count);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws Exception {

    }

    @GetMapping
    public ArrayList<User> getAllUsersList() {

    }
}

//электронная почта не может быть пустой и должна содержать символ @;
//логин не может быть пустым и содержать пробелы;
//имя для отображения может быть пустым — в таком случае будет использован логин;
//дата рождения не может быть в будущем.

