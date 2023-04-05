package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ExceptionHandlers;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends ExceptionHandlers {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> getAll() {
        return userService.getAll();
    }

    @PostMapping()
    public User add(@RequestBody User user) {
        return userService.add(user);
    }

    @PutMapping()
    public User update(@RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping("/{userId}")
    public User findById(@PathVariable int userId) {
        if (userService.findById(userId) == null) {
            throw new NotExistException(String.format("Пользователь с id=%s не найден", userId));
        }
        return userService.findById(userId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUsersFriends(@PathVariable int id) {
        return userService.getUsersFriends(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        if (!userService.addFriend(id, friendId)) {
            throw new NotExistException("id заданы некорректно!");
        }
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        if (!userService.deleteFriend(id, friendId)) {
            throw new NotExistException("id заданы некорректно!");
        }
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
