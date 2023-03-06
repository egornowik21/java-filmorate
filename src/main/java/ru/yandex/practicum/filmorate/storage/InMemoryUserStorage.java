package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    public ArrayList<User> findAll() {
        log.info("Получен запрос на получение списка пользователей");
        return new ArrayList<>(users.values());
    }

    public User create(@Valid User user) {
        if (users.containsKey(user.getId())) {
            log.error("Добавлен существующий пользователь");
            throw new ValidationException("Пользователь c id " +
                    user.getId() + " уже зарегистрирован.");
        } else {
            validateUser(user);
            user.setId(users.size() + 1);
        }
        users.put(user.getId(), user);
        log.info("Вы обновили текущего пользователя - {}",user.getId());
        return user;
    }

    public User put(@Valid User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь с id - {} не существует", user.getId());
            throw new NotFoundException("пользователя не существует");
        }
        validateUser(user);
        log.info("Вы обновили текущего пользователя - {}",user.getId());
        users.put(user.getId(), user);
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

    public Map<Integer, User> getUsers() {
        return users;
    }

    public User getUserbyId(Integer id) {
        return users.get(id);
    }
}
