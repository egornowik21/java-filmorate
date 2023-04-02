package ru.yandex.practicum.filmorate.daoint;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaDao {
    public List<Mpa> findAllMpa();

    public Mpa getMpaById(Integer mpa_id);

    public boolean mpaTableExists(int mpaid);

}
