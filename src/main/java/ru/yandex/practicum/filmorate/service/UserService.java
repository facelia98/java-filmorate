package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
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

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public List<User> getCommonFriends(int userId1, int userId2) {
        return userStorage.getCommonFriends(userId1, userId2);
    }

    public List<User> getUsersFriends(int id) {
        return userStorage.getUsersFriends(id);
    }
    public boolean addFriend(int userId1, int userId2) {
        return userStorage.addFriend(userId1, userId2);
    }

    public boolean deleteFriend(int userId1, int userId2) {
        return userStorage.deleteFriend(userId1, userId2);
    }

    public User findById(int userId) {
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
