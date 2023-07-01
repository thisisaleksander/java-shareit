package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserEmailAlreadyExistsException;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto save(UserDto userDto) throws UserEmailAlreadyExistsException;

    UserDto update(Integer userId, UserDto userDto);

    UserDto getBy(Integer userId);

    void delete(Integer userId);
}