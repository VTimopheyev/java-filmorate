package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class Rating {
    private int id;
    private String name;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("mpa_id", id);
        values.put("name", name);
        return values;
    }
}
