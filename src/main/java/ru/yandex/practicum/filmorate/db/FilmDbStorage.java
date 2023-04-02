package ru.yandex.practicum.filmorate.db;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.daoint.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage implements FilmDao {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreDbStorage, MpaDbStorage mpaDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDbStorage = genreDbStorage;
        this.mpaDbStorage = mpaDbStorage;
    }

    public List<Film> findAllFilms() {
        log.info("Получен запрос на получение списка фильмов");
        String sqlQuery = "select f.film_id, f.name, f.description, f.releaseDate, f.duration, f.raiting_id, m.mpa_name " +
                "from films f join mpa m on f.raiting_id = m.mpa_id ";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    public Film addFilm(Film film) {
        String sqlQuery = "insert into films (name,description, releaseDate, duration, raiting_id)" +
                "values (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        int id = keyHolder.getKey().intValue();
        film.setId(id);
        addGenres(film.getId(), film.getGenres());
        if (film.getGenres() != null) {
            film.setGenres(film.getGenres().stream()
                    .sorted((g1, g2) -> (int) (g1.getId() - g2.getId()))
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
        }
        return film;
    }

    public Film putFilm(@Valid Film film) {
        String sqlQuery = "update films set " +
                "name = ?, description = ? ,releaseDate = ? ,duration = ?, raiting_id=?" +
                "where film_id = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId());
        addGenres(film.getId(), film.getGenres());
        if (film.getGenres() != null) {
            film.setGenres(film.getGenres().stream()
                    .sorted((g1, g2) -> (int) (g1.getId() - g2.getId()))
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
        }
        return film;

    }

    public Film getFilmById(Integer filmId) {
        String sqlQuery = "select f.* " +
                "from films f join mpa m on f.raiting_id = m.mpa_id where f.film_id = ?";
        log.info("Выведен фильм по Id - {}", filmId);
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);
    }

    public void addUserLike(Integer filmid, Integer userId) {
        String sqlQuery = "insert into likes (film_id, user_id)" + "values (?,?)";
        log.info("Добавлен лайк на фильм - {}", filmid);
        jdbcTemplate.update(sqlQuery, filmid, userId);
    }

    public void deleteUserLike(Integer filmId, Integer userId) {
        String sqlQuery = "delete from likes where film_id = ? and user_id = ?";
        log.info("Удален лайк с фильма - {}", filmId);
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public List<Film> findTopFilms(Integer count) {
        String sqlQuery = "select  f.*, m.mpa_name,count(l.film_id) from films f " +
                "left join mpa m on f.raiting_id = m.mpa_id " +
                "left join likes l on f.film_id = l.film_id " +
                "group by f.film_id " +
                "order by count(l.film_id) desc limit ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    public boolean filmsTableExists(int filmid) {
        String sqlQuery = "select count(*) from films where film_id = ?";
        int result = jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmid);
        return result == 1;
    }

    private void addGenres(long filmId, Set<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            String sqlQuery = "delete from film_genre where film_id = ?";
            jdbcTemplate.update(sqlQuery, filmId);
            return;
        }
        List<Genre> genreListWithoutDuplicate = new ArrayList<>(genres);
        genreListWithoutDuplicate.sort((g1, g2) -> (int) (g1.getId() - g2.getId()));
        String sqlQuery = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
        jdbcTemplate.batchUpdate("insert into film_genre (genre_id, film_id) values (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, genreListWithoutDuplicate.get(i).getId());
                        ps.setLong(2, filmId);
                    }

                    @Override
                    public int getBatchSize() {
                        return genreListWithoutDuplicate.size();
                    }
                });
    }

    private Set<Genre> getGenresByFilmId(long filmId) {
        String sqlQuery = "select * from film_genre fg " +
                "left join genre g on fg.genre_id = g.genre_id where fg.film_id = ? ";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmId));
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("releaseDate").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(mpaDbStorage.getMpaById(resultSet.getInt("raiting_id")))
                .genres(getGenresByFilmId(resultSet.getInt("film_id")))
                .build();
    }


    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("genre_name"))
                .build();
    }
}
