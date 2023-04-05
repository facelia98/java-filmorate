package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest extends UserService {

    User user;

    public UserServiceTest() {
        super(new InMemoryUserStorage());
    }

    @BeforeEach
    void beforeEach() {
        user = new User();
    }

    @Test
    void validateTrue() {
        user.setId(0);
        user.setName("Имя пользователя");
        user.setLogin("login");
        user.setEmail("login@yandex.ru");
        user.setBirthday(LocalDate.now().minusYears(10L));
        assertTrue(validate(user));
    }

    @Test
    void validateFalseEmptyEmail() {
        user.setId(0);
        user.setName("Имя пользователя");
        user.setLogin("login");
        user.setEmail("");
        user.setBirthday(LocalDate.now().minusYears(10L));
        assertFalse(validate(user));
    }

    @Test
    void validateFalseBadEmail() {
        user.setId(0);
        user.setName("Имя пользователя");
        user.setLogin("login");
        user.setEmail("loginOyandex.ru");
        user.setBirthday(LocalDate.now().minusYears(10L));
        assertFalse(validate(user));
    }

    @Test
    void validateFalseBadLoginSpace() {
        user.setId(0);
        user.setName("Имя пользователя");
        user.setLogin("log in");
        user.setEmail("login@yandex.ru");
        user.setBirthday(LocalDate.now().minusYears(10L));
        assertFalse(validate(user));
    }

    @Test
    void validateTrueEmptyName() {
        user.setId(0);
        user.setName("");
        user.setLogin("login");
        user.setEmail("login@yandex.ru");
        user.setBirthday(LocalDate.now().minusYears(10L));
        assertTrue(validate(user));
    }

    @Test
    void validateFalseBadBirthdayDate() {
        user.setId(0);
        user.setName("Имя пользователя");
        user.setLogin("login");
        user.setEmail("login@yandex.ru");
        user.setBirthday(LocalDate.now().plusYears(10L));
        assertFalse(validate(user));
    }
}