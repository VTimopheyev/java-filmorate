package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidationException;

import java.time.LocalDate;
import java.util.HashMap;

@RestController("/users")
public class UserController {

    HashMap<Integer, User> users = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    public User createNewUser(@RequestBody User user) throws Exception {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            Exception e = new ValidationException("Email is wrong");
            log.debug(e.getMessage());
            throw e;
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            Exception e = new ValidationException("Login is wrong");
            log.debug(e.getMessage());
            throw e;
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            Exception e = new ValidationException("Birth date must not be in future");
            log.debug(e.getMessage());
            throw e;
        }

        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        log.info("New user created successfully");
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws Exception {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            Exception e = new ValidationException("Email is wrong");
            log.debug(e.getMessage());
            throw e;
        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            Exception e = new ValidationException("Login is wrong");
            log.debug(e.getMessage());
            throw e;
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            Exception e = new ValidationException("Birth date must not be in future");
            log.debug(e.getMessage());
            throw e;
        }

        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        log.info("New user updated successfully");
        return user;
    }

    @GetMapping
    public HashMap<Integer, User> getAllFilms() {
        log.info("Users list has been sent");
        return users;
    }
}

//электронная почта не может быть пустой и должна содержать символ @;
//логин не может быть пустым и содержать пробелы;
//имя для отображения может быть пустым — в таком случае будет использован логин;
//дата рождения не может быть в будущем.

