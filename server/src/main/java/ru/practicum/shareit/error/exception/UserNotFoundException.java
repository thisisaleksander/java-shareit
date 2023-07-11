package ru.practicum.shareit.error.exception;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(long userId) {
        super(String.format("User with id %d not found", userId));
    }
}