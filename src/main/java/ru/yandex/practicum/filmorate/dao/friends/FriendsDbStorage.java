package ru.yandex.practicum.filmorate.dao.friends;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class FriendsDbStorage implements FriendsStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getFriendsByUserId(long id) {
        return jdbcTemplate.query("SELECT * " +
                "FROM users AS u, friends AS f " +
                "WHERE u.user_id = f.friend_id " +
                "AND f.user_id = ? ", this::mapRowToUser, id);
    }

    @Override
    public void add(long userId, long friendId) {
        String friendsRows = "INSERT INTO friends (" +
                "friend_id," +
                "user_id) " +
                "VALUES (?,?) " +
                "ON CONFLICT DO NOTHING";
        jdbcTemplate.update(friendsRows, friendId, userId);
    }

    @Override
    public void delete(long userId, long friendId) {
        String deleteFriendsRow = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(deleteFriendsRow, userId, friendId);
    }

    @Override
    public List<User> getCommonFriends(long id, long friendId) {
        final String sqlQuery = "select * from USERS u, FRIENDS f, FRIENDS o " +
                "where u.USER_ID = f.FRIEND_ID AND u.USER_ID = o.FRIEND_ID AND f.USER_ID = ? AND o.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id, friendId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .login(resultSet.getString("login"))
                .email(resultSet.getString("email"))
                .name(resultSet.getString("name"))
                .birthday(Objects.requireNonNull(resultSet.getDate("birthday")).toLocalDate())
                .build();
    }
}
