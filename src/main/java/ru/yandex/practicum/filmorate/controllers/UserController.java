package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.InstanceNotFoundException;
import ru.yandex.practicum.filmorate.service.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {

    private HashMap<Integer, User> users = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private int count = 0;

    @PostMapping
    public User createNewUser(@RequestBody User user) throws ValidationException {
        if (validateUser(user)) {

            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            count++;
            user.setId(count);
            users.put(count, user);
            log.info("New user created successfully");
        }

        return users.get(count);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws Exception {
        if (validateUser(user) && checkUserExist(user)) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("New user updated successfully");
        }
        return users.get(user.getId());
    }

    @GetMapping
    public ArrayList<User> getAllUsers() {
        ArrayList<User> list = new ArrayList<>();
        for (int id : users.keySet()) {
            list.add(users.get(id));
        }
        log.info("Users list has been sent");
        return list;
    }

    private boolean validateUser(User user) throws ValidationException {
        boolean validated = true;

        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.info("Email is wrong");
            throw new ValidationException("Email is wrong");

        } else if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.info("Login is wrong");
            throw new ValidationException("Login is wrong");

        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Birth date must not be in future");
            throw new ValidationException("Birth date must not be in future");

        }
        return validated;
    }

    private boolean checkUserExist(User user) throws InstanceNotFoundException {
        if (users.containsKey(user.getId())) {
            return true;
        } else {
            log.info("No such user to update");
            throw new InstanceNotFoundException("No such user to update");
        }
    }
}

//электронная почта не может быть пустой и должна содержать символ @;
//логин не может быть пустым и содержать пробелы;
//имя для отображения может быть пустым — в таком случае будет использован логин;
//дата рождения не может быть в будущем.

