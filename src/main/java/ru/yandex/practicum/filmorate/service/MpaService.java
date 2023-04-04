package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@Slf4j
public class MpaService {
    private final MpaDao mpaDao;

    @Autowired
    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public List<Mpa> findAllMpa() {
        return mpaDao.findAllMpa();
    }

    public Mpa getMpaById(Integer mpaid) {
        if (!mpaDao.mpaTableExists(mpaid)) {
            throw new NotFoundException("Mpa - рейтинг не найден");
        }
        return mpaDao.getMpaById(mpaid);
    }
}
