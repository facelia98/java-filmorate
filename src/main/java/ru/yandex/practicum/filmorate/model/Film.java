package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
public class Film {
    @EqualsAndHashCode.Include
    int id;
    @EqualsAndHashCode.Exclude
    String name;
    @EqualsAndHashCode.Exclude
    String description;
    @EqualsAndHashCode.Exclude
    LocalDate releaseDate;
    @EqualsAndHashCode.Exclude
    Long duration;
}
