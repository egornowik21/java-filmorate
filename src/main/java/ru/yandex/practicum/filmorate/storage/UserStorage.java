package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Map;

public interface UserStorage {
    public ArrayList<User> findAll();
    public User create(@Valid User user);
    public User put(@Valid User user);
    public Map<Integer, User> getUsers();
    public User getUserbyId(Integer id);
}
