package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addUserLike(Integer filmid, Integer userId) {
        if (userId==null || filmid==null || !(userStorage.getUsers().containsKey(userId)) ||
                !(filmStorage.getFilms().containsKey(filmid))) {
            log.error("Пользователь с id - {} не существует", userId);
            log.error("Фильм с id - {} не существует", filmid);
        }
        log.info("Поставлен лайк на фильм - {}",filmid);
        User user = userStorage.getUsers().get(userId);
        Film film = filmStorage.getFilms().get(filmid);
        film.getLikes().add(user.getId());
    }

    public void deleteUserLike(Integer filmId, Integer userId) {
        if (userId==null || filmId==null ||!(userStorage.getUsers().containsKey(userId)) ||
                !(filmStorage.getFilms().containsKey(filmId))||userId<0||filmId<0) {
            log.error("Пользователь с id - {} не существует", userId);
            log.error("Фильм с id - {} не существует", filmId);
            throw new NotFoundException("Фильм или пользователь не найден");
        }
        log.info("Удален лайк с фильма - {}",filmId);
        filmStorage.getFilmbyId(filmId).getLikes().remove(userId);
    }

    public Set<Film> findTopFilms(Integer count) {
        return filmStorage.getFilms().values()
                .stream()
                .sorted(Comparator.comparing(Film::getLikesAmount).reversed())
                .limit(count)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public Film getFilmById(Integer filmId) {
        if (filmId==null || !(filmStorage.getFilms().containsKey(filmId))) {
            log.error("Фильм с id - {} не существует", filmId);
            throw new NotFoundException("фильм не найден");
        }
        log.info("Выведен фильм по Id - {}",filmId);
        return filmStorage.getFilmbyId(filmId);
    }
}
