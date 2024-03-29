package ru.yandex.practicum.filmorate.dao.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.models.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Mpa> getMpa(long id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select name from mpa where mpa_id = ?", id);

        if (mpaRows.next()) {
            String name = mpaRows.getString("name");
            Mpa mpa = new Mpa(id, name);

            log.info("Найден mpa: {} {}", id, name);

            return Optional.of(mpa);
        }
        log.info("MPA с идентификатором {} не найден.", id);
        return Optional.empty();
    }

    @Override
    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query("select * from mpa", this::mapRowToMpa);
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        long id = resultSet.getLong("mpa_id");
        String name = resultSet.getString("name");
        return new Mpa(id, name);
    }
}
