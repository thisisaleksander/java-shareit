package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.mapper.UserMapper.toDto;
import static ru.practicum.shareit.user.mapper.UserMapper.toUser;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserDto create(@Valid UserDto userDto) {
        log.info("[UserService] -> creating new user");
        User user = toUser(userDto);
        return toDto(userStorage.save(user));
    }

    public Set<UserDto> getAll() {
        log.info("[UserService] -> getting all users");
        return userStorage.getAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toSet());
    }

    public UserDto getBy(Integer userId) {
        User user = userStorage.getBy(userId);
        log.info("[UserService] -> getting user with id {}", userId);
        return toDto(user);
    }

    public UserDto update(UserDto userDto, Integer userId) {
        log.info("[UserService] -> updating user with id {}", userId);
        User user = toUser(userDto);
        return toDto(userStorage.update(user, userId));
    }

    public void delete(Integer userId) {
        log.info("[UserService] -> updating user with id {}", userId);
        userStorage.delete(userId);
    }
}
