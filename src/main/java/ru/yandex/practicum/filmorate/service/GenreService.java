package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@Slf4j
public class GenreService {

    private final GenreDao genreDao;

    @Autowired
    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public List<Genre> findAllGenre() {
        return genreDao.findAllGenre();
    }

    public Genre getGenreById(int genreid) {
        if (!genreDao.genreTableExists(genreid)) {
            throw new NotFoundException("Жанр не найден");
        }
        return genreDao.getGenreById(genreid);
    }
}
