package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserStorage {
    User save(User user);

    User update(User user, Integer id);

    List<User> getAll();

    User getBy(Integer id);

    void delete(Integer id);
}
