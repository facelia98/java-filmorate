package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonTypeId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
public class User {
    @EqualsAndHashCode.Include
    int id;
    @EqualsAndHashCode.Exclude
    String email;
    @EqualsAndHashCode.Exclude
    String login;
    @EqualsAndHashCode.Exclude
    String name;
    @EqualsAndHashCode.Exclude
    LocalDate birthday;
}
