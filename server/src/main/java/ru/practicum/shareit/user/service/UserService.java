package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User save(User user);

    User update(long userId, User user);

    User getUserById(long userId);

    void delete(long userId);
}