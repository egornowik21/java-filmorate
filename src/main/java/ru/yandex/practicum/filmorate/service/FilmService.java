package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class FilmService {

    private final UserDao userDao;
    private final FilmDao filmDao;

    @Autowired
    public FilmService(UserDao userDao, FilmDao filmDao) {
        this.userDao = userDao;
        this.filmDao = filmDao;
    }

    public List<Film> findAll() {
        return filmDao.findAllFilms();
    }

    public Film putFilm(@Valid Film film) {
        if (!filmDao.filmsTableExists(film.getId())) {
            throw new NotFoundException("Фильм не найден");
        }
        validateFilm(film);
        return filmDao.putFilm(film);
    }

    public Film createFilm(@Valid Film film) {
        validateFilm(film);
        return filmDao.addFilm(film);
    }

    public void addUserLike(Integer filmid, Integer userId) {
        if (!filmDao.filmsTableExists(filmid) || !userDao.userTableExists(userId)) {
            throw new NotFoundException("Фильм не найден");
        }
        filmDao.addUserLike(filmid, userId);
    }

    public void deleteUserLike(Integer filmId, Integer userId) {
        if (!filmDao.filmsTableExists(filmId) || !userDao.userTableExists(userId)) {
            throw new NotFoundException("Фильм не найден");
        }
        filmDao.deleteUserLike(filmId, userId);
    }

    public List<Film> findTopFilms(Integer count) {
        return filmDao.findTopFilms(count);
    }

    public Film getFilmById(Integer filmId) {
        if (!filmDao.filmsTableExists(filmId)) {
            throw new NotFoundException("Фильм не найден");
        }
        return filmDao.getFilmById(filmId);
    }

    private void validateFilm(@Valid Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Имя не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Длина описания фильма не может быть больше 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата фильма не может раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность должна быть положительной");
        }
    }
}
