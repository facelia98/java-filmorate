package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest extends FilmController {

    Film film;

    @BeforeEach
    void beforeEach() {
        film = new Film();
    }

    @Test
    void validateTrue() {
        film.setId(0);
        film.setName("Название фильма");
        film.setReleaseDate(LocalDate.now());
        film.setDescription("Описание");
        film.setDuration(100L);
        Assertions.assertTrue(validate(film));
    }

    @Test
    void validateFalseNullDescription() {
        film.setId(0);
        film.setName("Название фильма");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(100L);
        Assertions.assertFalse(validate(film));
    }

    @Test
    void validateFalseBlankName() {
        film.setId(0);
        film.setName("     ");
        film.setReleaseDate(LocalDate.now());
        film.setDescription("Описание");
        film.setDuration(100L);
        Assertions.assertFalse(validate(film));
    }

    @Test
    void validateFalseBadDuration() {
        film.setId(0);
        film.setName("Название фильма");
        film.setReleaseDate(LocalDate.now());
        film.setDescription("Описание");
        film.setDuration(-100L);
        Assertions.assertFalse(validate(film));
    }

    @Test
    void validateFalseBadReleaseDate() {
        film.setId(0);
        film.setName("Название фильма");
        film.setReleaseDate(LocalDate.now().minusYears(1000));
        film.setDescription("Описание");
        film.setDuration(100L);
        Assertions.assertFalse(validate(film));
    }

    @Test
    void validateFalseEmptyName() {
        film.setId(0);
        film.setName("");
        film.setReleaseDate(LocalDate.now());
        film.setDescription("Описание");
        film.setDuration(100L);
        Assertions.assertFalse(validate(film));
    }

    @Test
    void validateFalseNullReleaseDate() {
        film.setId(0);
        film.setName("Название фильма");
        film.setDescription("Описание");
        film.setDuration(100L);
        Assertions.assertFalse(validate(film));
    }

    @Test
    void validateFalseTooLongDescription() {
        film.setId(0);
        film.setName("Название фильма");
        film.setReleaseDate(LocalDate.now());
        film.setDescription("Очень длинное описание                         " +
                "                                                            " +
                "                                                            " +
                "                                                            ");
        film.setDuration(100L);
        Assertions.assertFalse(validate(film));
    }
}