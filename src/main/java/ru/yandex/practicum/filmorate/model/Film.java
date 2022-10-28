package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class Film {
    private int id;
    private final String name;
    private final Date releaseDate;
    private final String description;
    private final int duration;
    public Rating mpa;
    private List<Like> likes;
    private List<Genre> genres;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("duration", duration);
        values.put("release_date", releaseDate);
        values.put("mpa_id", mpa.getId());
        return values;
    }
}
