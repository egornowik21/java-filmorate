package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    public ArrayList<Film> findAll() {
        log.info("Получен запрос на получение списка фильмов");
        return new ArrayList<>(films.values());
    }

    public Film createFilm(@Valid Film film) {
        if (films.containsKey(film.getId())) {
            log.error("Добавлен существующий фильм");
            throw new ValidationException("Фильм c id " +
                    film.getId() + " уже зарегистрирован.");
        } else {
            validateFilm(film);
            film.setId(films.size() + 1);
        }
        log.info("Вы обновили текущего пользователя - {}",film.getId());
        films.put(film.getId(), film);
        return film;
    }

    public Film putFilm(@Valid Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Такого фильма нет");
            throw new NotFoundException("Фильма не существует");
        }
        validateFilm(film);
        films.put(film.getId(), film);
        log.info("Вы обновили текущего пользователя - {}",film.getId());
        return film;
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

    public Map<Integer, Film> getFilms() {
        return films;
    }

    public Film getFilmbyId(Integer id) {
        return films.get(id);
    }
}
