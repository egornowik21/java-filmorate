package ru.yandex.practicum.filmorate.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.daoint.MpaDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component("MpaDbStorageStorage")
public class MpaDbStorage implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mpa> findAllMpa() {
        log.info("Получен запрос на получение списка рейтингов");
        String sqlQuery = "select * from mpa";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    public Mpa getMpaById(Integer mpa_id) {
        String sqlQuery = "select * from mpa where mpa_id = ? ";
        log.info("Выведен рейтинг по Id - {}", mpa_id);
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, mpa_id);
    }

    public boolean mpaTableExists(int mpaid) {
        String sqlQuery = "select count(*) from mpa where mpa_id = ?";
        int result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, mpaid);
        return result == 1;
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("mpa_name"))
                .build();
    }
}
