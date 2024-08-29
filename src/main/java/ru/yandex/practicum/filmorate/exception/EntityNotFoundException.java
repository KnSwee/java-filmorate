package ru.yandex.practicum.filmorate.exception;

public class EntityNotFoundException extends RuntimeException {
    String error;
    String errorObject;

    public EntityNotFoundException(String message) {
        super(message);
    }
}
