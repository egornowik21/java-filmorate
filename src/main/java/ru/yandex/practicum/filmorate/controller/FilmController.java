package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    int count = 0;

    @GetMapping("/films")
    public ArrayList<Film> findAll() {
        log.info("Получен запрос на получение списка фильмов");
        return new ArrayList<>(films.values());
    }

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            log.error("Добавлен существующий фильм");
            throw new ValidationException("Фильм c id " +
                    film.getId() + " уже зарегистрирован.");
        } else {
            validateFilm(film);
            film.setId(films.size() + 1);
        }
        log.info("Вы - {}!", "добавили новый фильм");
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping("/films")
    public Film putFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Такого фильма нет");
            throw new ValidationException("Фильма не сущесьтвует");
        }
        validateFilm(film);
        films.put(film.getId(), film);
        log.info("Вы - {}!", "обновили данные для текущего фильма");
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
}
