package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Integer, User> userList = new HashMap<>();

    @Override
    public User findById(int userId) {
        return userList.get(userId);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userList.values());
    }

    @Override
    public User add(User user) {
        user.setId(userList.size() + 1);
        userList.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean addFriend(int userId1, int userId2) {
        if (userList.containsKey(userId1) && userList.containsKey(userId2)) {
            userList.get(userId1).addFriend(userId2);
            userList.get(userId2).addFriend(userId1);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteFriend(int userId1, int userId2) {
        if (userList.containsKey(userId1) && userList.containsKey(userId2)) {
            userList.get(userId1).deleteFriend(userId2);
            userList.get(userId2).deleteFriend(userId1);
            return true;
        }
        return false;
    }

    @Override
    public boolean update(User user) {
        if (userList.containsKey(user.getId())) {
            userList.put(user.getId(), user);
            return true;
        }
        return false;
    }

    @Override
    public List<User> getCommonFriends(int userId1, int userId2) {
        List<User> friendsId1 = userList.get(userId1).getFriends().stream()
                .map(userList::get)
                .collect(Collectors.toList());
        friendsId1.retainAll(userList.get(userId2).getFriends().stream()
                .map(userList::get)
                .collect(Collectors.toList()));
        return friendsId1;
    }

    @Override
    public List<User> getUsersFriends(int id) {
        List<Integer> friends = new ArrayList<>(userList.get(id).getFriends());
        return friends.stream()
                .map(userList::get)
                .collect(Collectors.toList());
    }
}
