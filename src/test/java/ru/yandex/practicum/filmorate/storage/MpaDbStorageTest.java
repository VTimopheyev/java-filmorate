package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTest {

    private final MpaDbStorage mpaDbStorage;

    @Test
    public void getMpaTest () {

        Optional<Rating> ratingOptional = Optional.ofNullable(mpaDbStorage.getRating(2));
        assertThat(ratingOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "PG")
                );
    }

    @Test
    public void getAllGenresTest (){
        Assertions.assertEquals(5, mpaDbStorage.getAllRatings().size());
    }
}
