package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier("dbSFilm")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getGenres(int filmId) {
        List<Genre> genres = new ArrayList<>();
        String query = "select g.\"Genre_ID\", g.\"Name\" " +
                "from \"Genres_to_Films\" gtf join \"Genres\" g on g.\"Genre_ID\" = gtf.\"Genre_ID\" " +
                "where gtf.\"Film_ID\" = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(query, filmId);
        while (genreRows.next()) {
            genres.add(Genre.of(genreRows.getInt("Genre_ID"), genreRows.getString("Name")));
        }
        return genres;
    }

    public void deleteGenres(int filmId) {
        String query = "delete from \"Genres_to_Films\" where \"Film_ID\" = ?";
        jdbcTemplate.update(query, filmId);
    }

    public void updateGenres(List<Genre> genres, int filmId) {
        deleteGenres(filmId);
        insertGenres(genres, filmId);
    }

    public void insertGenres(List<Genre> genres, int filmId) {
        //String query = "insert into \"Genres_to_Films\" (\"Film_ID\", \"Genre_ID\") " +
        // "values (?, ?)";
        String query = "INSERT INTO \"Genres_to_Films\" (\"Film_ID\", \"Genre_ID\") " +
                "SELECT ?, ? WHERE NOT EXISTS " +
                "(SELECT ? FROM \"Genres_to_Films\" WHERE \"Film_ID\" = ? AND \"Genre_ID\" = ?)";
        for (Genre g : genres) {
            jdbcTemplate.update(query, filmId, g.getId(), filmId, filmId, g.getId());
        }
    }

    @Override
    public Film findById(int filmId) {
        String query = "select f.\"Film_ID\", f.\"Name\", f.\"Description\", " +
                "f.\"Release_Date\", f.\"Duration\", r.\"Rating_ID\", r.\"Name\" as \"Mpa_Name\" " +
                "from \"Films\" f join \"Mpa\" r on f.\"Rating_ID\" = r.\"Rating_ID\" " +
                "where f.\"Film_ID\" = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(query, filmId);
        if (filmRows.next()) {
            return Film.of(
                    filmRows.getInt("Film_ID"),
                    filmRows.getString("Name"),
                    filmRows.getString("Description"),
                    filmRows.getTimestamp("Release_Date").toLocalDateTime().toLocalDate(),
                    (long) filmRows.getInt("Duration"),
                    Mpa.of(filmRows.getInt("Rating_ID"), filmRows.getString("Mpa_Name")),
                    getGenres(filmId));
        }
        return null;
    }

    @Override
    public List<Film> getAll() {
        String query = "select f.\"Film_ID\", f.\"Name\", f.\"Description\", " +
                "f.\"Release_Date\", f.\"Duration\", r.\"Rating_ID\", r.\"Name\" as \"Mpa_Name\" " +
                "from \"Films\" f join \"Mpa\" r on f.\"Rating_ID\" = r.\"Rating_ID\"";
        List<Film> result = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(query);
        while (filmRows.next()) {
            result.add(Film.of(
                    filmRows.getInt("Film_ID"),
                    filmRows.getString("Name"),
                    filmRows.getString("Description"),
                    filmRows.getTimestamp("Release_Date").toLocalDateTime().toLocalDate(),
                    (long) filmRows.getInt("Duration"),
                    Mpa.of(filmRows.getInt("Rating_ID"), filmRows.getString("Mpa_Name")),
                    getGenres(filmRows.getInt("Film_ID"))));
        }
        return result;
    }

    @Override
    public List<Film> getTop(int count) {
        String query = "select f.\"Film_ID\", f.\"Name\", f.\"Description\", " +
                "f.\"Release_Date\", f.\"Duration\", r.\"Rating_ID\", r.\"Name\" \"Mpa_Name\", " +
                "(select count(*) from \"Film_Likes\" l where l.\"Film_ID\" = f.\"Film_ID\") likes " +
                "from \"Films\" f join \"Mpa\" r on f.\"Rating_ID\" = r.\"Rating_ID\"" +
                "group by f.\"Film_ID\" " +
                "order by likes desc limit ?";
        List<Film> result = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(query, count);
        while (filmRows.next()) {
            result.add(Film.of(
                    filmRows.getInt("Film_ID"),
                    filmRows.getString("Name"),
                    filmRows.getString("Description"),
                    filmRows.getTimestamp("Release_Date").toLocalDateTime().toLocalDate(),
                    (long) filmRows.getInt("Duration"),
                    Mpa.of(filmRows.getInt("Rating_ID"), filmRows.getString("Mpa_Name")),
                    getGenres(filmRows.getInt("Film_ID"))));
        }
        return result;
    }

    @Override
    public Film add(Film film) {
        String query = "insert into \"Films\" (\"Name\", \"Description\", " +
                "\"Rating_ID\", \"Release_Date\", \"Duration\") " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, film.getName());
            pst.setString(2, film.getDescription());
            pst.setInt(3, film.getMpa().getId());
            pst.setDate(4, Date.valueOf(film.getReleaseDate()));
            pst.setInt(5, film.getDuration().intValue());
            return pst;
        }, keyHolder) != 0) {
            film.setId(keyHolder.getKey().intValue());
            if (film.getGenres() != null) insertGenres(film.getGenres(), film.getId());
            return film;
        }
        return null;
    }

    @Override
    public boolean update(Film film) {
        String query = "update \"Films\" set \"Name\" = ?, " +
                "\"Description\" = ?, \"Rating_ID\" = ?, " +
                "\"Release_Date\" = ?, \"Duration\" = ?" +
                "where \"Film_ID\" = ?";
        if (jdbcTemplate.update(query, film.getName(), film.getDescription(),
                film.getMpa().getId(), film.getReleaseDate(), film.getDuration(), film.getId()) > 0) {
            if (film.getGenres() != null) updateGenres(film.getGenres(), film.getId());
            return true;
        }
        return false;
    }

    @Override
    public boolean addLike(int filmId, int userId) {
        if (findById(filmId) != null) {
            String query = "insert into \"Film_Likes\" (\"User_ID\", \"Film_ID\") values (?, ?)";
            if (jdbcTemplate.update(query, userId, filmId) != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteLike(int filmId, int userId) {
        if (findById(filmId) != null) {
            String query = "delete from \"Film_Likes\" where \"Film_ID\" = ? and \"User_ID\" = ?";
            if (jdbcTemplate.update(query, filmId, userId) > 0) {
                return true;
            }
        }
        return false;
    }
}
