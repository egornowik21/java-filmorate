package ru.yandex.practicum.filmorate.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component("GenreDbStorage")
public class GenreDbStorage implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> findAllGenre() {
        log.info("Получен запрос на получение списка жанров");
        String sqlQuery = "select * from genre";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    public Genre getGenreById(Integer genreid) {
        String sqlQuery = "select * from genre where genre_id = ? ";
        log.info("Выведен жанр по Id - {}", genreid);
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, genreid);
    }

    public boolean genreTableExists(int genreid) {
        String sqlQuery = "select count(*) from genre where genre_id = ?";
        int result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, genreid);
        return result == 1;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("genre_name"))
                .build();
    }
}
