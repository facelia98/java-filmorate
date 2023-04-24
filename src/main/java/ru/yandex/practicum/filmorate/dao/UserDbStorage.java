package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier("dbSUser")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User findById(int userId) {
        String query = "select * from \"Users\" where \"User_ID\" = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(query, userId);
        if (userRows.next()) {
            return User.of(
                    userRows.getInt("User_ID"),
                    userRows.getString("Email"),
                    userRows.getString("Login"),
                    userRows.getString("Name"),
                    userRows.getTimestamp("Birthday").toLocalDateTime().toLocalDate()
            );
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        String query = "select * from \"Users\"";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(query);
        List<User> users = new ArrayList<>();
        while (userRows.next()) {
            users.add(User.of(
                    userRows.getInt("User_ID"),
                    userRows.getString("Email"),
                    userRows.getString("Login"),
                    userRows.getString("Name"),
                    userRows.getTimestamp("Birthday").toLocalDateTime().toLocalDate()
            ));
        }
        return users;
    }

    @Override
    public User add(User user) {
        String query = "insert into \"Users\" (\"Email\", \"Login\", \"Name\", \"Birthday\") " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();


        if (jdbcTemplate.update(connection -> {
            PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, user.getEmail());
            pst.setString(2, user.getLogin());
            pst.setString(3, user.getName());
            pst.setDate(4, Date.valueOf(user.getBirthday()));
            return pst;
        }, keyHolder) != 0) {
            user.setId(keyHolder.getKey().intValue());
            return user;
        }
        return null;
    }

    @Override
    public boolean addFriend(int userId1, int userId2) {
        if (findById(userId1) != null && findById(userId2) != null) {
            String query = "insert into \"Friends\" (\"User_A\", \"User_B\") values (?, ?)";
            if (jdbcTemplate.update(query, userId1, userId2) != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteFriend(int userId1, int userId2) {
        if (findById(userId1) != null && findById(userId2) != null) {
            String query = "delete from \"Friends\" where \"User_A\" = ? and \"User_B\" = ?";
            if (jdbcTemplate.update(query, userId1, userId2) != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update(User user) {
        String query = "update \"Users\" set \"Email\" = ?, \"Login\" = ?, " +
                "\"Name\" = ?, \"Birthday\" = ? where \"User_ID\" = ?";
        if (jdbcTemplate.update(query,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId()) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<User> getCommonFriends(int userId1, int userId2) {
        List<User> users = new ArrayList<>();
        String query = "select f1.\"User_B\" from \"Friends\" f1 where f1.\"User_A\" = ? " +
                "INTERSECT select f2.\"User_B\" from \"Friends\" f2 where f2.\"User_A\" = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(query, userId1, userId2);
        while (userRows.next()) {
            users.add(findById(userRows.getInt("User_B")));
        }
        return users;
    }

    @Override
    public List<User> getUsersFriends(int id) {
        List<User> users = new ArrayList<>();
        String query = "select \"User_B\" from \"Friends\" where \"User_A\" = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(query, id);
        while (userRows.next()) {
            users.add(findById(userRows.getInt("User_B")));
        }
        return users;
    }
}
