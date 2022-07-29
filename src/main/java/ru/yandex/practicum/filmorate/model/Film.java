package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Film {

    private int id;
    private final String name;
    private final LocalDate releaseDate;
    private final String description;
    private final int duration;
    private List<Integer> likesList;
}
