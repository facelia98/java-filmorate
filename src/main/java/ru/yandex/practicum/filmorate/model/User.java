package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor(staticName = "of")
public class User {
    private final Set<Integer> friends = new HashSet<>();
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public void addFriend(int id) {
        friends.add(id);
    }

    public void deleteFriend(int id) {
        friends.remove(id);
    }
}
