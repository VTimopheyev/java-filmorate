package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InstanceNotFoundException extends RuntimeException {


    public InstanceNotFoundException(String message) {

        super(message);
    }
}