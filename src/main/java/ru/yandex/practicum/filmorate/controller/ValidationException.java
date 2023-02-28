package ru.yandex.practicum.filmorate.controller;

public class ValidationException extends RuntimeException {
    public ValidationException(String s) {
        super(s);
    }
}
