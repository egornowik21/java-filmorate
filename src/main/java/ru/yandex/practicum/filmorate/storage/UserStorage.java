package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {

    public Map<Integer, User> getUsers();

    public User getUserbyId(Integer id);
}
