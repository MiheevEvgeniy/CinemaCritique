package ru.yandex.practicum.filmorate.dao.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class GenresDbStorage implements GenresStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenresDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genre> getGenre(long id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select name from genres where genre_id = ?", id);

        if (genreRows.next()) {
            String name = genreRows.getString("name");
            Genre genre = new Genre(id, name);

            log.info("Найден жанр: {} {}", id, name);

            return Optional.of(genre);
        }
        log.info("Жанр с идентификатором {} не найден.", id);
        return Optional.empty();
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("select * from genres", this::mapRowToGenre);
    }

    @Override
    public List<Genre> getGenresByFilmId(long id) {
        return jdbcTemplate.query("SELECT * FROM film_genres WHERE film_id = ?", this::mapRowToGenre, id);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        long id = resultSet.getLong("genre_id");
        String name = getGenre(id).get().getName();
        return new Genre(id, name);
    }
}
