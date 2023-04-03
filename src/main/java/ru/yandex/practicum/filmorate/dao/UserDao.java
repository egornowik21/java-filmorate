package ru.yandex.practicum.filmorate.dao;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.model.User;


import java.util.List;

public interface UserDao {
    public List<User> findAll();

    public User create(User user);

    public User put(@Valid User user);

    public void addFriend(Integer userId, Integer friendId);

    public void deleteFriend(Integer userId, Integer friendId);

    public List<User> getFriends(Integer userId);

    public List<User> commonFriends(Integer userId1, Integer userId2);

    public User getUserById(Integer userId);

    public boolean userTableExists(int userid);
}
