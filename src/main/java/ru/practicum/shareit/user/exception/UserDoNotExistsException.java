package ru.practicum.shareit.user.exception;

public class UserDoNotExistsException extends RuntimeException {
    public UserDoNotExistsException(String message) {
        super(message);
    }
}
