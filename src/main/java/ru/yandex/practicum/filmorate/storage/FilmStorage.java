package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film findById(int filmId);

    List<Film> getAll();

    List<Film> getTop(int count);

    Film add(Film film);

    boolean update(Film film);

    boolean addLike(int filmId, int userId);

    boolean deleteLike(int filmId, int userId);
}
