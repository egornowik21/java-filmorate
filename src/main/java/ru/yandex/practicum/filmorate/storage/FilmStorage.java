package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.Map;

public interface FilmStorage {
    public ArrayList<Film> findAll();
    public Film createFilm(@Valid Film film);
    public Film putFilm(@Valid Film film);
    public Map<Integer, Film> getFilms();
    public Film getFilmbyId(Integer id);
}
