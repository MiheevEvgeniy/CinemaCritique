package ru.yandex.practicum.filmorate.dao.likes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer getLikesByFilmId(long filmId) {
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("SELECT COUNT(user_id) as likes_count from film_liked_users where film_id = ?", filmId);

        if (likesRows.next()) {
            int likes = likesRows.getInt("likes_count");

            log.info("Найдены лайки для фильма: film id: {}; likes: {}.", filmId, likes);

            return likes;
        }
        log.info("Лайки для фильма с идентификатором {} не найдены", filmId);
        return null;
    }

    public void deleteLike(long userId, long filmId) {
        String likesRows = "DELETE FROM film_liked_users " +
                "WHERE film_id=? AND user_id =?";
        jdbcTemplate.update(likesRows, filmId, userId);
        log.info("Удаление лайка выполнено");
    }

    public void addLike(long userId, long filmId) {
        String likesRows = "INSERT INTO film_liked_users (" +
                "film_id," +
                "user_id) " +
                "VALUES (?,?) ";
        jdbcTemplate.update(likesRows, filmId, userId);
        log.info("Добавление лайка выполнено");
    }

    public List<Film> getPopularFilms(int count) {
        return jdbcTemplate.query("SELECT f.*, m.*, " +
                "(SELECT COUNT(flu.film_id) " +
                "FROM film_liked_users as flu " +
                "WHERE flu.film_id = f.film_id) as likes " +
                "FROM films as f, MPA as m " +
                "WHERE f.mpa_id = m.mpa_id " +
                "GROUP BY f.film_id " +
                "ORDER BY likes DESC " +
                "LIMIT ?", this::mapRowToFilm, count);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .description(resultSet.getString("description"))
                .name(resultSet.getString("name"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .rate(resultSet.getInt("rate"))
                .duration(resultSet.getLong("duration"))
                .mpa(Mpa.builder().id(resultSet.getInt("MPA.mpa_id")).name(resultSet.getString("MPA.name")).build())
                .build();
    }
}
