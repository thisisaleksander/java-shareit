package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.InvalidUserEmailException;
import ru.practicum.shareit.user.exception.UserEmailAlreadyExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static ru.practicum.shareit.user.mapper.UserMapper.toUser;
import static ru.practicum.shareit.user.mapper.UserMapper.toUserDto;
import static ru.practicum.shareit.validation.EmailValidator.isValidEmail;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    private boolean isEmailAlreadyExists(String email) {
        User user = repository.findByEmail(email);
        return user != null;
    }
    @Override
    public List<UserDto> getAll() {
        return repository.findAll().stream()
                .map(UserMapper::toUserDto)
                .sorted(Comparator.comparing(UserDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto save(@Valid UserDto userDto) throws UserEmailAlreadyExistsException {
        User user = toUser(userDto);
        if (isEmailAlreadyExists(user.getEmail())) {
            throw new UserEmailAlreadyExistsException(String.join(" ",
                    "[UserServiceImpl] -> email", user.getEmail(), "already exists"));
        }
        if (!isValidEmail(user.getEmail())) {
            throw new InvalidUserEmailException("[UserServiceImpl] -> email is invalid:");
        }
        return toUserDto(repository.save(user));
    }

    @Override
    @Transactional
    public UserDto update(@NotNull Integer userId, @Valid UserDto userDto) {
        User user = toUser(userDto);
        log.info("[UserServiceImpl] -> user from DTO successfully created");
        user.setId(userId);
        Optional<User> originalUser = repository.findById(userId);
        if (originalUser.isPresent()) {
            if (nonNull(user.getName()) && !user.getName().isEmpty()) {
                originalUser.get().setName(user.getName());
                log.info("[UserServiceImpl] -> updated name of user with id {}", userId);
            }
            if (nonNull(user.getEmail()) && !user.getEmail().isEmpty()) {
                if(!isEmailAlreadyExists(user.getEmail())) {
                    if (isValidEmail(user.getEmail())) {
                        originalUser.get().setEmail(user.getEmail());
                        log.info("[UserServiceImpl] -> updated name of user with id {}", userId);
                    } else {
                        log.info("[UserServiceImpl] -> invalid user email {}", userId);
                        throw new InvalidUserEmailException(String.join(" ",
                                "[UserServiceImpl] -> email", user.getEmail(), "already exists"));
                    }
                } else {
                    throw new UserEmailAlreadyExistsException(String.join(" ",
                            "[UserServiceImpl] -> email", user.getEmail(), "already exists"));
                }
            }
            return toUserDto(repository.save(originalUser.get()));
        } else {
            log.info("[UserServiceImpl] -> user with id {} not fount", userId);
            throw new UserNotFoundException(String.join(" ",
                    "[UserServiceImpl] -> user with id", userId.toString(), "do not found"));
        }
    }

    @Override
    public UserDto getBy(@NotNull Integer userId) {
        return toUserDto(repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.join(" ",
                        "[UserServiceImpl] -> user with id", userId.toString(), "do not found"))));
    }

    @Override
    @Transactional
    public void delete(@NotNull Integer userId) {
        log.info("[UserServiceImpl] -> trying to delete user with id {} in database", userId);
        if (repository.findById(userId).isPresent()) {
            repository.deleteById(userId);
        } else {
            log.info("[UserServiceImpl] -> user with id {} not fount", userId);
            throw new UserNotFoundException(String.join(" ",
                    "[UserServiceImpl] -> user with id", userId.toString(), "do not found"));
        }
    }
}