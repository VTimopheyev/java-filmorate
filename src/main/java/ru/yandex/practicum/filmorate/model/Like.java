package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class Like {
    private int id;
    private int filmId;
    private int userId;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("like_id", id);
        values.put("film_id", filmId);
        values.put("user_id", userId);
        return values;
    }
}
