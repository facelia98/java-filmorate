package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public FilmService(@Qualifier("dbSFilm") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getAll() {
        log.info("Получен GET-запрос всех фильмов");
        return filmStorage.getAll();
    }

    public List<Film> getTop(int count) {
        if (count < 0) {
            log.error("Передано отрицательное значение count в GET-запросе на ТОП фильмов", count);
            throw new ValidationException("Передано значение меньше 0");
        }
        log.info("Получен GET-запрос ТОП фильмов", count);
        return filmStorage.getTop(Integer.parseInt(String.valueOf(count)));
    }

    public Film findById(int filmId) {
        if (filmStorage.findById(filmId) == null) {
            log.error("Получен GET-запрос на поиск фильма по некорректному id", filmId);
            throw new NotExistException(String.format("Фильм с id=%s не найден!", filmId));
        }
        log.info("Получен GET-запрос на поиск фильма по id", filmId);
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
            return findById(film.getId());
        }
        log.error("Полученный POST-запрос некорректен (фильм не существует):", film);
        throw new NotExistException("Фильм не существует!");
    }

    public void addLike(int filmId, int userId) {
        if (!filmStorage.addLike(filmId, userId)) {
            log.error("Получен PUT-запрос на добавление лайка: некорректный id",
                    String.format("[filmId=%s, userId=%s]", filmId, userId));
            throw new NotExistException("Указаны неверные id!");
        }
        log.error("Получен PUT-запрос на добавление лайка",
                String.format("[filmId=%s, userId=%s]", filmId, userId));
    }

    public void deleteLike(int filmId, int userId) {
        if (!filmStorage.deleteLike(filmId, userId)) {
            log.error("Получен DELETE-запрос на удаление лайка: некорректный id",
                    String.format("[filmId=%s, userId=%s]", filmId, userId));
            throw new NotExistException("Указаны неверные id!");
        }
        log.error("Получен DELETE-запрос на удаление лайка",
                String.format("[filmId=%s, userId=%s]", filmId, userId));
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
