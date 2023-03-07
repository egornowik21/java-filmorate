package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
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
    public ArrayList<Film> findAll() {
        log.info("Получен запрос на получение списка фильмов");
        return new ArrayList<>(filmStorage.getFilms().values());
    }
    public Film putFilm(@Valid Film film) {
        if (!filmStorage.getFilms().containsKey(film.getId())) {
            log.error("Такого фильма нет");
            throw new NotFoundException("Фильма не существует");
        }
        filmStorage.getFilms().put(film.getId(), film);
        log.info("Вы обновили текущего пользователя - {}",film.getId());
        return film;
    }

    public Film createFilm(@Valid Film film) {
        if (filmStorage.getFilms().containsKey(film.getId())) {
            log.error("Добавлен существующий фильм");
            throw new ValidationException("Фильм c id " +
                    film.getId() + " уже зарегистрирован.");
        } else {
            validateFilm(film);
            film.setId(filmStorage.getFilms().size() + 1);
        }
        log.info("Вы обновили текущего пользователя - {}",film.getId());
        filmStorage.getFilms().put(film.getId(), film);
        return film;
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
