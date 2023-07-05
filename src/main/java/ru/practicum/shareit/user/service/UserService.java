package ru.practicum.shareit.user.service;

import ru.practicum.shareit.error.exception.AlreadyExistException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User save(User user) throws AlreadyExistException;

    User update(long userId, User user) throws ValidationException;

    User getByUserId(long userId);

    void delete(long userId);
}