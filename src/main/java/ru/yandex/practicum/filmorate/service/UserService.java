package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class UserService {

    UserStorage userStorage;

    public UserService(@Qualifier("dbSUser") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAll() {
        log.info("Получен GET-запрос всех пользователей");
        return userStorage.getAll();
    }

    public List<User> getCommonFriends(int userId1, int userId2) {
        if (userStorage.findById(userId1) != null && userStorage.findById(userId2) != null) {
            log.info("Получен GET-запрос общих друзей",
                    String.format("[id1=%s, id2=%s]", userId1, userId2));
            return userStorage.getCommonFriends(userId1, userId2);
        }
        log.error("Получен GET-запрос общих друзей: некорректный id",
                String.format("[id1=%s, id2=%s]", userId1, userId2));
        throw new NotExistException("id заданы некорректно!");
    }

    public List<User> getUsersFriends(int id) {
        if (userStorage.findById(id) != null) {
            log.info("Получен GET-запрос на список друзей пользователя по id", id);
            return userStorage.getUsersFriends(id);
        }
        log.error("Получен GET-запрос на список друзей пользователя по некорректному id", id);
        throw new NotExistException(String.format("Пользователь с id=%s не найден", id));
    }

    public void addFriend(int userId1, int userId2) {
        if (!userStorage.addFriend(userId1, userId2)) {
            log.error("Получен PUT-запрос на добавление друга: некорректный id",
                    String.format("[id1=%s, id2=%s]", userId1, userId2));
            throw new NotExistException("id заданы некорректно!");
        }
        log.info("Получен PUT-запрос на добавление друга",
                String.format("[id1=%s, id2=%s]", userId1, userId2));
    }

    public void deleteFriend(int userId1, int userId2) {
        if (!userStorage.deleteFriend(userId1, userId2)) {
            log.error("Получен DELETE-запрос на удаление друга: некорректный id",
                    String.format("[id1=%s, id2=%s]", userId1, userId2));
            throw new NotExistException("id заданы некорректно!");
        }
        log.info("Получен DELETE-запрос на удаление друга",
                String.format("[id1=%s, id2=%s]", userId1, userId2));
    }

    public User findById(int userId) {
        if (userStorage.findById(userId) == null) {
            log.error("Получен GET-запрос на поиск пользователя по некорректному id", userId);
            throw new NotExistException(String.format("Пользователь с id=%s не найден", userId));
        }
        log.info("Получен GET-запрос на поиск пользователя по id", userId);
        return userStorage.findById(userId);
    }

    public User add(User user) {
        if (validate(user)) {
            log.info("Получен POST-запрос на добавление пользователя:", user);
            return userStorage.add(user);
        }
        log.error("Полученный POST-запрос некорректен (данные не прошли валидацию):", user);
        throw new ValidationException("Данные пользователя некорректны!");

    }

    public User update(User user) {
        if (userStorage.update(user)) {
            log.info("Получен POST-запрос на обновление данных пользователя:", user);
            return user;
        }
        log.error("Полученный POST-запрос некорректен (пользователь не существует):", user);
        throw new NotExistException("Пользователь не существует!");
    }

    protected boolean validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        return (user.getLogin() != null)
                && (user.getEmail() != null)
                && (!user.getEmail().isBlank())
                && (!user.getLogin().isBlank())
                && (!user.getLogin().contains(" "))
                && (!user.getEmail().contains(" "))
                && (user.getEmail().contains("@"))
                && (!user.getBirthday().isAfter(LocalDate.now()));
    }

}
