package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@Service
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> findAll() {
        log.info("Получен запрос на получение списка пользователей");
        return userDao.findAll();
    }

    public User create(@Valid User user) {
        validateUser(user);
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.info("Вы добавили нового пользователя - {}", user.getId());
        return userDao.create(user);
    }

    public User put(@Valid User user) {
        if (!userDao.userTableExists(user.getId())) {
            throw new NotFoundException("Пользователь не найден");
        }
        validateUser(user);
        log.info("Вы обновили текущего пользователя - {}", user.getId());
        return userDao.put(user);
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
        if (!userDao.userTableExists(userId) || !userDao.userTableExists(friendId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        log.info("Вы добавили нового друга");
        userDao.addFriend(userId, friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        if (!userDao.userTableExists(userId) || !userDao.userTableExists(friendId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        log.info("Вы удалили друга");
        userDao.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(Integer userId) {
        if (!userDao.userTableExists(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        log.info("Вы получили список друзей");
        return userDao.getFriends(userId);
    }

    public List<User> commonFriends(Integer userId1, Integer userId2) {
        if (!userDao.userTableExists(userId1) || !userDao.userTableExists(userId2)) {
            throw new NotFoundException("Пользователь не найден");
        }
        log.info("Выведен список общих друзей");
        return userDao.commonFriends(userId1, userId2);
    }

    public User getUserById(Integer userId) {
        if (!userDao.userTableExists(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        log.info("Выведен пользователь по Id - {}", userId);
        return userDao.getUserById(userId);
    }
}
