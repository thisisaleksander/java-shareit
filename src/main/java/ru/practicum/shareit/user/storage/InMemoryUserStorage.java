package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.InvalidUserEmailException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.exception.UserEmailAlreadyExistsException;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.practicum.shareit.validation.EmailValidator.isValidEmail;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {
    private Integer localId = 0;
    private final Map<Integer, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    private Integer getNewId() {
        return ++localId;
    }

    @Override
    public User save(User user) {
        if (emails.contains(user.getEmail())) {
            throw new UserEmailAlreadyExistsException(String.join(" ",
                    "[InMemoryUserStorage] -> user with", user.getEmail(), "already exists")
            );
        }
        if (isNull(user.getEmail()) || user.getEmail().isEmpty()) {
            throw new InvalidUserEmailException("[InMemoryUserStorage] -> no email found in request");
        }
        if (!isValidEmail(user.getEmail())) {
            throw new InvalidUserEmailException("[InMemoryUserStorage] -> invalid email found in request");
        }
        user.setId(getNewId());
        users.put(user.getId(), user);
        log.info("[InMemoryUserStorage] -> created new user with id {}", user.getId());
        emails.add(user.getEmail());
        log.info("[InMemoryUserStorage] -> new email added to know list: {}", user.getId());
        return user;
    }

    @Override
    public User update(User user, Integer id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException(String.join(" ",
                    "[InMemoryUserStorage] -> user with id", id.toString(), "do not exists"));
        }
        if (nonNull(user.getEmail()) && !user.getEmail().isEmpty()) {
            if (isValidEmail(user.getEmail())) {
                if (emails.contains(user.getEmail()) && !Objects.equals(user.getEmail(), users.get(id).getEmail())) {
                    throw new UserEmailAlreadyExistsException(String.join(" ",
                            "[InMemoryUserStorage] -> user with", user.getEmail(), "already exists"));
                }
                log.info("[InMemoryUserStorage] -> updated email of user with id {}", user.getId());
                emails.remove(users.get(id).getEmail());
                users.get(id).setEmail(user.getEmail());
                emails.add(user.getEmail());
            } else {
                throw new InvalidUserEmailException("[InMemoryUserStorage] -> invalid email found in request");
            }
        }
        if (nonNull(user.getName()) && !user.getName().isEmpty()) {
            log.info("[InMemoryUserStorage] -> updated name of user with id {}", user.getId());
            users.get(id).setName(user.getName());
        }
        log.info("[InMemoryUserStorage] -> data of user with id {} updated", user.getId());
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("[InMemoryUserStorage] -> total users found: {}", users.size());
        return null;
    }

    @Override
    public User getBy(Integer id) {
        if (users.containsKey(id)) {
            log.info("[InMemoryUserStorage] -> found user with id {}", id);
            return users.get(id);
        } else {
            throw new UserNotFoundException(String.join(" ",
                    "[InMemoryUserStorage] -> user with id", id.toString(), "do not exists"));
        }
    }

    @Override
    public void delete(Integer id) {
        if (users.containsKey(id)) {
            String emailToDelete = users.get(id).getEmail();
            users.remove(id);
            log.info("[InMemoryUserStorage] -> user with id {} deleted", id);
            emails.remove(emailToDelete);
            log.info("[InMemoryUserStorage] -> email {} deleted from known list", emailToDelete);
        } else {
            throw new UserNotFoundException(String.join(" ",
                    "[InMemoryUserStorage] -> user with id", id.toString(), "do not exists"));
        }
    }
}
