package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto save(UserDto userDto);

    UserDto update(Integer userId, UserDto userDto);

    UserDto getBy(Integer userId);

    void delete(Integer userId);
}