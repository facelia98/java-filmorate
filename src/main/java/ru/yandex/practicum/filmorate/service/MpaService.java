package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@Slf4j
public class MpaService {
    MpaDbStorage mpaDbStorage;

    @Autowired
    public MpaService(@Qualifier("dbSMpa") MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public List<Mpa> getAll() {
        log.info("Получен GET-запрос всех рейтингов");
        return mpaDbStorage.getAll();
    }

    public Mpa findById(int id) {
        if (mpaDbStorage.getByID(id) == null) {
            log.error("Получен GET-запрос на поиск рейтинга по некорректному id", id);
            throw new NotExistException(String.format("Рейтинг с id=%s не найден!", id));
        }
        log.info("Получен GET-запрос на поиск фильма по id", id);
        return mpaDbStorage.getByID(id);
    }
}
