package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class FilmService {

    FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public List<Film> getTop(int count) {
        return filmStorage.getTop(count);
    }

    public Film findById(int filmId) {
        return filmStorage.findById(filmId);
    }

    public Film add(Film film) {
        if (validate(film)) {
            log.info("Получен POST-запрос на добавление фильма:", film);
            return filmStorage.add(film);
        }
        log.error("Полученный POST-запрос некорректен (данные не прошли валидацию):", film);
        throw new ValidationException("Данные фильма некорректны!");
    }

    public Film update(Film film) {
        if (filmStorage.update(film)) {
            log.info("Получен POST-запрос на обновление данных фильма:", film);
            return film;
        }
        log.error("Полученный POST-запрос некорректен (фильм не существует):", film);
        throw new NotExistException("Фильм не существует!");
    }


    public boolean addLike(int filmId, int userId) {
        return filmStorage.addLike(filmId, userId);
    }

    public boolean deleteLike(int filmId, int userId) {
        return filmStorage.deleteLike(filmId, userId);
    }

    protected boolean validate(Film film) {
        return (film.getDescription() != null)
                && (film.getDescription().length() <= 200)
                && (!film.getName().isBlank())
                && (film.getName() != null)
                && (film.getDuration() > 0)
                && (film.getReleaseDate() != null)
                && (film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28)));
    }
}
