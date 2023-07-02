package ru.practicum.shareit.user.service;

import ru.practicum.shareit.error.exception.AlreadyExistException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto save(UserDto userDto) throws AlreadyExistException, ValidationException;

    UserDto update(Integer userId, UserDto userDto) throws ValidationException;

    UserDto getBy(Integer userId);

    void delete(Integer userId);
}