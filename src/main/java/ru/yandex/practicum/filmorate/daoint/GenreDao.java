package ru.yandex.practicum.filmorate.daoint;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {
    public List<Genre> findAllGenre();

    public Genre getGenreById(Integer genreid);

    public boolean genreTableExists(int genreid);
}
