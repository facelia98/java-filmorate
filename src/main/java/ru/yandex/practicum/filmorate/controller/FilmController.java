package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    List<Film> filmList = new ArrayList<>();

    @GetMapping("/all")
    public List<Film> getAll() {
        return filmList;
    }

    @PostMapping("/add")
    public Film add(@RequestBody Film film) throws ValidationException {
        if (validate(film)) {
            film.setId(filmList.size() + 1);
            filmList.add(film);
            log.info("Получен POST-запрос на добавление фильма:", film);
            return film;
        } else {
            log.error("Полученный POST-запрос некорректен (данные не прошли валидацию):", film);
            throw new ValidationException("Данные фильма некорректны!");
        }
    }

    @PutMapping("/update")
    public Film update(@RequestBody Film film) throws NotExistException {
        if (filmList.contains(film)) {
            filmList.remove(film);
            filmList.add(film);
            log.info("Получен POST-запрос на обновление данных фильма:", film);
            return film;
        } else {
            log.error("Полученный POST-запрос некорректен (фильм не существует):", film);
            throw new NotExistException("Фильм не существует!");
        }
    }

    protected boolean validate(Film film) {
        return (film.getDescription().length() <= 200)
                && (!film.getName().equals(""))
                && (film.getName() != null)
                && (film.getDuration() > 0)
                && (film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)));
    }
}
