package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.error.exception.UserNotFoundException;
import ru.practicum.shareit.user.User;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Transactional
    @Override
    public User save(User user) {
        log.info("[UserService] -> saving new user");
        return repository.save(user);
    }

    @Transactional
    @Override
    public User update(long userId, User user) {
        user.setId(userId);
        User userTemp = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        if (user.getName() == null) {
            user.setName(userTemp.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(userTemp.getEmail());
        }
        log.info("[UserService] -> user with id {} updated", userId);
        return repository.save(user);
    }

    @Override
    public User getByUserId(long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Transactional
    @Override
    public void delete(long userId) {
        log.info("[UserService] -> deleting user with id {}", userId);
        repository.deleteById(userId);
    }
}