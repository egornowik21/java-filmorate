package ru.yandex.practicum.filmorate.controller;

public class ValidationException extends Throwable {
    public ValidationException(String s) {
        super(s);
    }
}
