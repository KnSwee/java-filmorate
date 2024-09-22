package ru.yandex.practicum.filmorate.exception;

public class EntityNotFoundException extends RuntimeException {
    private String error;
    private String errorObject;

    public EntityNotFoundException(String message) {
        super(message);
    }
}
