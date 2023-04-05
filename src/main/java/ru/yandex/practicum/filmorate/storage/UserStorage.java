package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User findById(int userId);

    List<User> getAll();

    User add(User user);

    boolean addFriend(int userId1, int userId2);

    boolean deleteFriend(int userId1, int userId2);

    boolean update(User user);

    List<User> getCommonFriends(int userId1, int userId2);

    List<User> getUsersFriends(int id);

}
