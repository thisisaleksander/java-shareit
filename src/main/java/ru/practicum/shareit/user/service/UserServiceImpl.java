package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.InvalidUserEmailException;
import ru.practicum.shareit.user.exception.UserEmailAlreadyExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static java.util.Objects.nonNull;
import static ru.practicum.shareit.validation.EmailValidator.isValidEmail;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserStorage {
    private final UserRepository repository;

    @Override
    @Transactional
    public User save(@Valid User user) throws UserEmailAlreadyExistsException {
        log.info("[UserServiceImpl] -> saving new user to database");
        return repository.save(user);
    }

    @Override
    @Transactional
    public User update(@Valid User user, @NotNull Integer id) throws InvalidUserEmailException,
            UserNotFoundException, UserEmailAlreadyExistsException {
        User originalUser = repository.findUserById(id);
        if (nonNull(user.getEmail()) && !user.getEmail().isEmpty()) {
            if (!isValidEmail(user.getEmail())) {
                throw new UserEmailAlreadyExistsException(String.join(" ",
                            "[UserServiceImpl] -> email ", user.getEmail(), " already exists"));
            }
        } else {
            log.info("[UserServiceImpl] -> updated name of user with id {}", id);
            user.setEmail(originalUser.getEmail());
        }
        if (nonNull(user.getName()) && !user.getName().isEmpty()) {
            log.info("[UserServiceImpl] -> updated name of user with id {}", id);
        } else {
            user.setName(originalUser.getName());
        }
        log.info("[UserServiceImpl] -> data of user with id {} updated", id);
        return repository.save(user);
    }

    @Override
    public List<User> getAll() {
        log.info("[UserServiceImpl] -> getting all users from database");
        return repository.findAll();
    }

    @Override
    public User getBy(@NotNull Integer id) throws UserNotFoundException {
        log.info("[UserServiceImpl] -> trying to find user with id {} in database", id);
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.join(" ",
                        "[UserServiceImpl] -> user with id", id.toString(), "do not exists")));
    }

    @Override
    @Transactional
    public void delete(@NotNull Integer id) throws UserNotFoundException {
        log.info("[UserServiceImpl] -> trying to delete user with id {} in database", id);
        repository.deleteById(id);
    }
}
