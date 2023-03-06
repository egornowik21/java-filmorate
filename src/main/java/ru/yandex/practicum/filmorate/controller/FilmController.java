package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;
    @Autowired
    public FilmController(FilmStorage filmStorage,FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public ArrayList<Film> findAll() {
        return filmStorage.findAll();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        return filmStorage.createFilm(film);
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        return filmStorage.putFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putFilmsLikes(@PathVariable Integer id,@PathVariable Integer userId) {
        filmService.addUserLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteFilmLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.deleteUserLike(id,userId);
    }

    @GetMapping("/popular")
    public Set<Film> getTopFilms(@RequestParam(defaultValue = "10",required = false) Integer count) {
        return filmService.findTopFilms(count);
    }
}
