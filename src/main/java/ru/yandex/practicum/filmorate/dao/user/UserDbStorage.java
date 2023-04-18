package ru.yandex.practicum.filmorate.dao.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UpdateException;
import ru.yandex.practicum.filmorate.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public User add(User user) {
        String userRows = "INSERT INTO users (" +
                "email," +
                "login," +
                "name," +
                "birthday)" +
                "VALUES (?,?,?,?);";
        jdbcTemplate.update(userRows,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        String friendsRows = "INSERT INTO friends (" +
                "user_id," +
                "friend_id)" +
                "VALUES (?,?);";
        List<Long> friends = user.getFriends();
        for (int i = 0; i < friends.size(); i++) {
            jdbcTemplate.update(friendsRows, user.getId(), friends.get(i));
        }
        SqlRowSet userIdRows = jdbcTemplate.queryForRowSet("select MAX(user_id) as id from users");

        if (userIdRows.next()) {
            long userId = userIdRows.getLong("id");
            user.setId(userId);
        }
        log.info("Пользователь добавлен");
        return user;
    }

    @Override
    public User update(User user) {
        if (!getUser(user.getId()).isPresent()) {
            throw new UpdateException("Ошибка обновления пользователя");
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
        String deleteFriendsRow = "DELETE FROM friends WHERE user_id = ?";
        jdbcTemplate.update(deleteFriendsRow, user.getId());
        String friendsRows = "INSERT INTO friends (" +
                "friend_id," +
                "user_id) " +
                "VALUES (?,?) " +
                "ON CONFLICT DO NOTHING";
        List<Long> friends = user.getFriends();
        for (int i = 0; i < friends.size(); i++) {
            jdbcTemplate.update(friendsRows, friends.get(i), user.getId());
        }
        log.info("Пользователь обновлен");
        return user;
    }

    @Override
    public Optional<User> getUser(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where user_id = ?", id);

        if (userRows.next()) {
            long userId = userRows.getLong("user_id");
            String email = userRows.getString("email");
            String login = userRows.getString("login");
            String name = userRows.getString("name");
            LocalDate birthday = null;
            if (userRows.getDate("birthday") != null) {
                birthday = Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate();
            }
            SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("select friend_id from friends where user_id = ?", id);
            User user = User.builder()
                    .id(userId)
                    .login(login)
                    .email(email)
                    .name(name)
                    .birthday(birthday)
                    .build();
            while (friendsRows.next()) {
                long friendId = friendsRows.getLong("friend_id");
                user.getFriends().add(friendId);
            }

            log.info("Найден пользователь: {} {}", user.getId(), user.getName());

            return Optional.of(user);
        }
        log.info("Пользователь с идентификатором {} не найден.", id);
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("select * from users", this::mapRowToUser);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        long userId = resultSet.getLong("user_id");
        String email = resultSet.getString("email");
        String login = resultSet.getString("login");
        String name = resultSet.getString("name");
        LocalDate birthday = null;
        if (resultSet.getDate("birthday") != null) {
            birthday = Objects.requireNonNull(resultSet.getDate("birthday")).toLocalDate();
        }
        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("select friend_id from friends where user_id = ?", userId);
        User user = User.builder()
                .id(userId)
                .login(login)
                .email(email)
                .name(name)
                .birthday(birthday)
                .build();
        while (friendsRows.next()) {
            long friendId = friendsRows.getLong("friend_id");
            user.getFriends().add(friendId);
        }

        return user;
    }
}
