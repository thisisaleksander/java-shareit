package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.exception.AlreadyExistException;
import ru.practicum.shareit.error.exception.UserNotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Transactional
    @Override
    public User save(User user) throws AlreadyExistException {
        log.info("[UserService] -> saving new user");
        return repository.save(user);
    }

    @Transactional
    @Override
    public User update(long userId, User user) throws ValidationException {
        user.setId(userId);
        Optional<User> userTemp = repository.findById(userId);
        if (user.getName() == null) {
            user.setName(userTemp.get().getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(userTemp.get().getEmail());
        }
        log.info("[UserService] -> user with id {} updated", userId);
        return repository.save(user);
    }

    @Override
    public User getByUserId(long userId) {
        log.info("[UserService] -> trying to find user with id {}", userId);
        return repository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("[UserService] -> user with id {} not found", userId);
                    return new UserNotFoundException(userId);
                });
    }

    @Transactional
    @Override
    public void delete(long userId) {
        log.info("[UserService] -> deleting user with id {}", userId);
        repository.deleteById(userId);
    }
}