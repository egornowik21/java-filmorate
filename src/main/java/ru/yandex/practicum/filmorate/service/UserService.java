package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public ArrayList<User> findAll() {
        log.info("Получен запрос на получение списка пользователей");
        return new ArrayList<>(userStorage.getUsers().values());
    }

    public User create(@Valid User user) {
        if (userStorage.getUsers().containsKey(user.getId())) {
            log.error("Добавлен существующий пользователь");
            throw new ValidationException("Пользователь c id " +
                    user.getId() + " уже зарегистрирован.");
        } else {
            validateUser(user);
            user.setId(userStorage.getUsers().size() + 1);
        }
        userStorage.getUsers().put(user.getId(), user);
        log.info("Вы обновили текущего пользователя - {}",user.getId());
        return user;
    }

    public User put(@Valid User user) {
        if (!userStorage.getUsers().containsKey(user.getId())) {
            log.error("Пользователь с id - {} не существует", user.getId());
            throw new NotFoundException("пользователя не существует");
        }
        validateUser(user);
        log.info("Вы обновили текущего пользователя - {}",user.getId());
        userStorage.getUsers().put(user.getId(), user);
        return user;
    }

    private void validateUser(@Valid User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Неверный формат почты");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем времени");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }
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
