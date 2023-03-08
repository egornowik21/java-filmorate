package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private int id;

    @Email(message = "Не является почтовым адресом")
    @NotBlank(message = "Почтовый адрес не может быть пустым")
    @NotNull(message = "Почтовый адрес не может быть пустым")
    private String email;

    @NotNull(message = "Логин не может быть пустым")
    @NotBlank(message = "Логин не может быть пустым")
    private String login;

    private String name;

    @PastOrPresent()
    private LocalDate birthday;
    private final Set<Integer> friends = new HashSet<>();
}
