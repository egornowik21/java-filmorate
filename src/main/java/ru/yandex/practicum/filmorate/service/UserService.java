package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Integer userId, Integer friendId) {
        if (userId==null || friendId==null || !(userStorage.getUsers().containsKey(userId)) ||
                !(userStorage.getUsers().containsKey(friendId))) {
            log.error("Пользователь с id - {} не существует", userId,friendId);
            throw new NotFoundException("Пользователя не существует");
        }
        User user = userStorage.getUsers().get(userId);
        User friend = userStorage.getUsers().get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        if (userId==null || friendId==null || !(userStorage.getUsers().containsKey(userId)) ||
                !(userStorage.getUsers().containsKey(friendId))) {
            log.error("Пользователь с id - {} не существует", userId,friendId);
            throw new NotFoundException("Пользователя не существует");
        }
        User user = userStorage.getUsers().get(userId);
        User friend = userStorage.getUsers().get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getFrinds(Integer userId) {
        if (userId==null || !(userStorage.getUsers().containsKey(userId))) {
            log.error("Пользователь с id - {} не существует", userId);
            throw new NotFoundException("Пользователя не существует");
        }
        List<User> friendList = new ArrayList<>();
        for (Integer id:userStorage.getUserbyId(userId).getFriends()) {
            friendList.add(userStorage.getUserbyId(id));
        }
        return friendList;
    }

    public List<User> commonFriends(Integer userId1, Integer userId2) {
        if (!(userStorage.getUsers().containsKey(userId1)) ||
                !(userStorage.getUsers().containsKey(userId2)) ||userId1<0 ||userId2<0) {
            log.error("Пользователь с id - {} не существует", userId1,userId2);
            throw new NotFoundException("Пользователя не существует");
        }
        List<User> commonFrineds = new ArrayList<>();
        for(Integer friendId: userStorage.getUserbyId(userId1).getFriends()) {
            if (userStorage.getUserbyId(userId2).getFriends().contains(friendId)) {
                commonFrineds.add(userStorage.getUserbyId(friendId));
            }
        }
        log.info("Выведен список общих друзей");
        return commonFrineds;
    }

    public User getUserById(Integer userId) {
        if (userId==null || !(userStorage.getUsers().containsKey(userId))) {
            log.error("Пользователь с id - {} не существует", userId);
            throw new NotFoundException("Пользователя не существует");
        }
        log.info("Выведен пользователь по Id - {}",userId);
        return userStorage.getUserbyId(userId);
    }
}
