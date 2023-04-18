package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier("dbSMpa")
public class MpaDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa getByID(int id) {
        String query = "select * from \"Mpa\" where \"Rating_ID\" = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(query, id);
        if (genreRows.next()) {
            return Mpa.of(genreRows.getInt("Rating_ID"),
                    genreRows.getString("Name"));
        }
        return null;
    }

    public List<Mpa> getAll() {
        List<Mpa> mpas = new ArrayList<>();
        String query = "select * from \"Mpa\"";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(query);
        while (genreRows.next()) {
            mpas.add(Mpa.of(genreRows.getInt("Rating_ID"),
                    genreRows.getString("Name")));
        }
        return mpas;
    }
}
