package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.filmorate.exceptions.ExceptionHandlers;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends ExceptionHandlers {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Film> getAll() {
        return filmService.getAll();
    }

    @PostMapping()
    public Film add(@RequestBody Film film) {
        return filmService.add(film);
    }

    @PutMapping()
    public Film update(@RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/{filmId}")
    public Film findById(@PathVariable int filmId) {
        if (filmService.findById(filmId) == null) {
            throw new NotExistException(String.format("Фильм с id=%s не найден!", filmId));
        }
        return filmService.findById(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getTop(@RequestParam(required = false) String count) {
        if (count != null) {
            if (Integer.parseInt(count) < 0) {
                throw new ValidationException("Передано значение меньше 0");
            }
            return filmService.getTop(Integer.parseInt(count));
        }
        return filmService.getTop(10);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        RestTemplate restTemplate = new RestTemplate();
        User user = restTemplate.getForObject("http://localhost:8080/users/{id}", User.class, Map.of("id", userId));
        if (user == null || !filmService.addLike(id, userId)) {
            throw new NotExistException("Указаны неверные id!");
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        if (!filmService.deleteLike(id, userId)) {
            throw new NotExistException("Указаны неверные id!");
        }
    }


}
