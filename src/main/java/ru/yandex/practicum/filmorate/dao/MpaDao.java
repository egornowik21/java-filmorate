package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaDao {
    public List<Mpa> findAllMpa();

    public Mpa getMpaById(Integer mpaid);

    public boolean mpaTableExists(int mpaid);

}
