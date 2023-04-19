package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class Mpa {
    private Integer id;
    private String name;

    public Mpa(Integer id) {
        this.id = id;
    }
}
