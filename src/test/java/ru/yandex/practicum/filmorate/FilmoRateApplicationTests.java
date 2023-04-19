package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = "classpath:data.sql")
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;

    @Test
    void getAllFilmsTest() {
        Film film1 = new Film();
        film1.setName("Название фильма");
        film1.setMpa(new Mpa(2));
        film1.setReleaseDate(LocalDate.now());
        film1.setDescription("Описание");
        film1.setDuration(100L);
        filmStorage.add(film1);
        Film film2 = new Film();
        film2.setName("Название фильма2");
        film2.setMpa(new Mpa(1));
        film2.setReleaseDate(LocalDate.now().minusYears(2));
        film2.setDescription("Описание");
        film2.setDuration(50L);
        filmStorage.add(film2);
        Collection<Film> films = filmStorage.getAll();

        assertThat(films).hasSize(5);
    }

    @Test
    void createFilm_CorrectIdTest() {
        Film film1 = new Film();
        film1.setName("Название фильма");
        film1.setMpa(new Mpa(1));
        film1.setReleaseDate(LocalDate.now());
        film1.setDescription("Описание");
        film1.setDuration(100L);
        filmStorage.add(film1);
        Film film = filmStorage.findById(1);

        assertEquals(film.getId(), 1);
    }

    @Test
    public void getAllUsersTest() {
        User user1 = new User();
        user1.setName("Имя пользователя");
        user1.setLogin("login");
        user1.setEmail("login@yandex.ru");
        user1.setBirthday(LocalDate.now().minusYears(10L));
        userStorage.add(user1);
        User user2 = new User();
        user2.setName("Имя пользователя2");
        user2.setLogin("login2");
        user2.setEmail("login2@yandex.ru");
        user2.setBirthday(LocalDate.now().minusYears(20L));
        userStorage.add(user2);
        Collection<User> users = userStorage.getAll();

        assertThat(users).hasSize(2);
    }

    @Test
    public void makeFriendsTest() {
        User user1 = new User();
        user1.setName("Имя пользователя");
        user1.setLogin("login");
        user1.setEmail("login@yandex.ru");
        user1.setBirthday(LocalDate.now().minusYears(10L));
        userStorage.add(user1);
        User user2 = new User();
        user2.setName("Имя пользователя2");
        user2.setLogin("login2");
        user2.setEmail("login2@yandex.ru");
        user2.setBirthday(LocalDate.now().minusYears(20L));
        userStorage.add(user2);
        userStorage.addFriend(1, 2);
        List<User> u1Friends = userStorage.getUsersFriends(1);
        List<User> u2Friends = userStorage.getUsersFriends(2);
        assertThat(u1Friends).hasSize(1);
        assertThat(u2Friends).hasSize(0);
    }

    @Test
    public void makeLike() {
        Film film1 = new Film();
        film1.setName("Название фильма");
        film1.setReleaseDate(LocalDate.now());
        film1.setMpa(Mpa.of(1, "G"));
        film1.setDescription("Описание");
        film1.setDuration(100L);
        filmStorage.add(film1);
        Film film2 = new Film();
        film2.setName("Название фильма2");
        film2.setMpa(Mpa.of(1, "G"));
        film2.setReleaseDate(LocalDate.now().minusYears(2));
        film2.setDescription("Описание");
        film2.setDuration(50L);
        filmStorage.add(film2);
        User user1 = new User();
        user1.setName("Имя пользователя");
        user1.setLogin("login");
        user1.setEmail("login@yandex.ru");
        user1.setBirthday(LocalDate.now().minusYears(10L));
        userStorage.add(user1);
        assertTrue(filmStorage.addLike(1, 1));
    }
}