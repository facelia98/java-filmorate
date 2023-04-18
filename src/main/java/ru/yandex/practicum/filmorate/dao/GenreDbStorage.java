package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier("dbSGenre")
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre getByID(int id) {
        String query = "select * from \"Genres\" where \"Genre_ID\" = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(query, id);
        if (genreRows.next()) {
            return Genre.of(genreRows.getInt("Genre_ID"),
                    genreRows.getString("Name"));
        }
        return null;
    }

    public List<Genre> getAll() {
        List<Genre> genres = new ArrayList<>();
        String query = "select * from \"Genres\"";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(query);
        while (genreRows.next()) {
            genres.add(Genre.of(genreRows.getInt("Genre_ID"),
                    genreRows.getString("Name")));
        }
        return genres;
    }
}
