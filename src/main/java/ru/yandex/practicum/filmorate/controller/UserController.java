package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    List<User> userList = new ArrayList<>();

    @GetMapping("/all")
    public List<User> getAll() {
        return userList;
    }

    @PostMapping("/add")
    public User add(@RequestBody User user) throws ValidationException {
        if (validate(user)) {
            user.setId(userList.size() + 1);
            userList.add(user);
            log.info("Получен POST-запрос на добавление пользователя:", user);
            return user;
        } else {
            log.error("Полученный POST-запрос некорректен (данные не прошли валидацию):", user);
            throw new ValidationException("Данные пользователя некорректны!");
        }
    }

    @PutMapping("/update")
    public User update(@RequestBody User user) throws NotExistException {
        if (userList.contains(user)) {
            userList.remove(user);
            userList.add(user);
            log.info("Получен POST-запрос на обновление данных пользователя:", user);
            return user;
        } else {
            log.error("Полученный POST-запрос некорректен (пользователь не существует):", user);
            throw new NotExistException("Пользователь не существует!");
        }
    }

    protected boolean validate(User user) {
        return (!user.getEmail().equals(""))
                && (!user.getLogin().equals(""))
                && (!user.getLogin().contains(" "))
                && (user.getLogin() != null)
                && (user.getEmail() != null)
                && (!user.getEmail().contains(" "))
                && (user.getEmail().contains("@"))
                && (!user.getBirthday().isAfter(LocalDate.now()));
    }
}
