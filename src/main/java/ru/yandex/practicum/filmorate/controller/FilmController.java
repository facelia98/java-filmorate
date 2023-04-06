package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;

    @Autowired
    public FilmController(FilmService filmService, UserService userService) {
        this.filmService = filmService;
        this.userService = userService;
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
        return filmService.findById(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getTop(@RequestParam(defaultValue = "10") int count) {
        return filmService.getTop(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        User user = userService.findById(userId);
        filmService.addLike(id, user.getId());
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(id, userId);
    }


}
