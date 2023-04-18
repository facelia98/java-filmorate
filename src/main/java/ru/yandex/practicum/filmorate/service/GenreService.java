package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@Slf4j
public class GenreService {
    GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(@Qualifier("dbSGenre") GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public List<Genre> getAll() {
        log.info("Получен GET-запрос всех жанров");
        return genreDbStorage.getAll();
    }

    public Genre findById(int id) {
        if (genreDbStorage.getByID(id) == null) {
            log.error("Получен GET-запрос на поиск жанра по некорректному id", id);
            throw new NotExistException(String.format("Жанр с id=%s не найден!", id));
        }
        log.info("Получен GET-запрос на поиск жанра по id", id);
        return genreDbStorage.getByID(id);
    }
}
