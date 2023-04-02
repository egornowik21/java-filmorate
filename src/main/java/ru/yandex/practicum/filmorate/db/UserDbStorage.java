package ru.yandex.practicum.filmorate.db;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.daoint.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.util.List;

@Slf4j
@Component("userDbStorage")
public class UserDbStorage implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAll() {
        String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    public User create(User user) {
        String sqlQuery = "insert into users(email, name,login,birthday) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        user.setId(id);
        return user;
    }

    public User put(@Valid User user) {
        String sqlQuery = "update users set " +
                "email = ?, name = ? ,login = ? ,birthday = ?" +
                "where user_id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getName(), user.getLogin(), user.getBirthday(), user.getId());
        return user;
    }

    public void addFriend(Integer userId, Integer friendId) {
        String sqlQuery = "insert into friends (user_id, friends_id)" + "values (?,?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        String sqlQuery = "delete from friends where user_id = ? and friends_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    public List<User> getFriends(Integer userId) {
        String sqlQuery = "select u.* from users u join friends f on u.user_id = f.friends_id where " +
                "f.user_id = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId);
    }

    public List<User> commonFriends(Integer userId1, Integer userId2) {
        String sqlQuery = "select * from (select friends_id from friends where user_id = ?) as f1 " +
                "join (select friends_id from friends where user_id = ?) as f2 on f1.friends_id = f2.friends_id " +
                "join users u on u.user_id = f1.friends_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId1, userId2);
    }

    public User getUserById(Integer userId) {
        String sqlQuery = "select * from users where user_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, userId);
    }

    public boolean userTableExists(int userid) {
        String sqlQuery = "select count(*) from users where user_id = ?";
        int result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, userid);
        return result == 1;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}
