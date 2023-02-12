package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    int count = 0;

    @GetMapping("/users")
    public ArrayList<User> findAll() {
        log.info("Получен запрос на получение списка пользователей");
        return new ArrayList<>(users.values());
    }

    public void validateUser(@Valid User user) throws ValidationException {
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

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())) {
            log.error("Добавлен существующий пользователь");
            throw new ValidationException("Пользователь c id " +
                    user.getId() + " уже зарегистрирован.");
        }
        else {
            validateUser(user);
            user.setId(users.size()+1);
        }
        users.put(user.getId(), user);
        log.info("Вы - {}!", "добавили нового пользователя");
        return user;
    }

    @PutMapping("/users")
    public User put(@Valid @RequestBody User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            log.error("Такого пользователя нет");
            throw new ValidationException("пользователя не существует");
        }
        validateUser(user);
        log.info("Вы - {}!", "обновили текущего пользователя");
        users.put(user.getId(), user);
        return user;
    }
}
