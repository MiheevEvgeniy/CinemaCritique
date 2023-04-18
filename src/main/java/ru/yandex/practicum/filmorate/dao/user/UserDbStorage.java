package ru.yandex.practicum.filmorate.dao.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundDataException;
import ru.yandex.practicum.filmorate.models.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FriendsStorage friendsStorage;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, @Qualifier("friendsDbStorage") FriendsStorage friendsStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendsStorage = friendsStorage;
    }

    @Override
    public User add(User user) {
        String userRows = "INSERT INTO users (" +
                "email," +
                "login," +
                "name," +
                "birthday)" +
                "VALUES (?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(userRows, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());

        log.info("Пользователь добавлен");
        return user;
    }

    @Override
    public User update(User user) {
        if (user == null || getUser(user.getId()).isEmpty()) {
            throw new NotFoundDataException("Пользователь не найден");
        }
        String sqlQuery = "UPDATE users SET " +
                "email=?," +
                "login=?," +
                "name=?," +
                "birthday=?" +
                "WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        saveFriends(user);
        log.info("Пользователь обновлен");
        return user;
    }

    private void saveFriends(User user) {
        String deleteFriendsRow = "DELETE FROM friends WHERE user_id = ?";
        jdbcTemplate.update(deleteFriendsRow, user.getId());
        String friendsRows = "INSERT INTO friends (" +
                "friend_id," +
                "user_id) " +
                "VALUES (?,?) " +
                "ON CONFLICT DO NOTHING";
        List<User> friends = friendsStorage.getFriendsByUserId(user.getId());
        for (int i = 0; i < friends.size(); i++) {
            jdbcTemplate.update(friendsRows, friends.get(i).getId(), user.getId());
        }
    }

    @Override
    public Optional<User> getUser(long id) {
        final String sqlQuery = "SELECT * FROM users u " +
                "WHERE user_id = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
        if (users.size() != 1) {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundDataException("user id=" + id);
        }
        log.info("Пользователь с идентификатором {} найден.", id);
        return Optional.of(users.get(0));
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("select * from users", this::mapRowToUser);
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
