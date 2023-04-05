package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Integer, Film> filmList = new HashMap<>();

    @Override
    public Film findById(int filmId) {
        return filmList.get(filmId);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(filmList.values());
    }

    @Override
    public List<Film> getTop(int count) {
        return filmList.values().stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size()))
                .limit(count).collect(Collectors.toList());
    }

    @Override
    public Film add(Film film) {
        film.setId(filmList.size() + 1);
        filmList.put(film.getId(), film);
        return film;
    }

    @Override
    public boolean update(Film film) {
        if (filmList.containsKey(film.getId())) {
            filmList.put(film.getId(), film);
            return true;
        }
        return false;
    }

    @Override
    public boolean addLike(int filmId, int userId) {
        if (filmList.containsKey(filmId)) {
            filmList.get(filmId).addLike(userId);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteLike(int filmId, int userId) {
        if (filmList.containsKey(filmId)) {
            if (filmList.get(filmId).getLikes().contains(userId)) {
                filmList.get(filmId).deleteLike(userId);
                return true;
            }
        }
        return false;
    }
}
