package ru.yandex.practicum.filmorate.dao;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.model.Film;


import java.util.List;

public interface FilmDao {
    public List<Film> findAllFilms();

    public Film addFilm(Film film);

    public Film putFilm(@Valid Film film);

    public Film getFilmById(Integer filmId);

    public void addUserLike(Integer filmid, Integer userId);

    public void deleteUserLike(Integer filmId, Integer userId);

    public List<Film> findTopFilms(Integer count);

    public boolean filmsTableExists(int filmid);
}
