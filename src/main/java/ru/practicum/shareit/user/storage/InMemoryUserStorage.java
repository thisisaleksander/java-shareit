package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.InvalidUserEmailException;
import ru.practicum.shareit.user.exception.UserDoNotExistsException;
import ru.practicum.shareit.user.exception.UserEmailAlreadyExistsException;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.practicum.shareit.validation.EmailValidator.isValidEmail;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {
    private Integer localId;
    private final Map<Integer, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    private Integer getNewId() {
        if (localId == null) {
            localId = 0;
        }
        return ++localId;
    }

    @Override
    public User save(User user) {
        if (emails.contains(user.getEmail())) {
            throw new UserEmailAlreadyExistsException(String.join(" ",
                    "Пользователь с почтой", user.getEmail(), "уже существует.")
            );
        }
        if (isNull(user.getEmail()) || user.getEmail().isEmpty()) {
            throw new InvalidUserEmailException("Отсутствует email");
        }
        if (!isValidEmail(user.getEmail())) {
            throw new InvalidUserEmailException("Передана не правильная почта");
        }
        user.setId(getNewId());
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        log.info("User created with id = " + user.getId());
        return user;
    }

    @Override
    public User update(User user, Integer id) {
        if (!users.containsKey(id)) {
            throw new UserDoNotExistsException(String.join(" ",
                    "Пользователя с id", id.toString(), "не существует."));
        }
        if (nonNull(user.getEmail()) && !user.getEmail().isEmpty()) {
            if (isValidEmail(user.getEmail())) {
                if (emails.contains(user.getEmail()) && !Objects.equals(user.getEmail(), users.get(id).getEmail())) {
                    throw new UserEmailAlreadyExistsException(String.join(" ",
                            "Пользователь с почтой", user.getEmail(), "уже существует."));
                }
                log.info("User email updated, id = " + id);
                emails.remove(users.get(id).getEmail());
                users.get(id).setEmail(user.getEmail());
                emails.add(user.getEmail());
            } else {
                throw new InvalidUserEmailException("Передана не правильная почта");
            }
        }
        if (nonNull(user.getName()) && !user.getName().isEmpty()) {
            log.info("User name updated, id = " + id);
            users.get(id).setName(user.getName());
        }
        log.info("User updated with id = " + id);
        return users.get(id);
    }

    @Override
    public Set<User> getAll() {
        log.info("Total users found: " + users.size());
        return new HashSet<>(users.values());
    }

    @Override
    public User getBy(Integer id) {
        if (users.containsKey(id)) {
            log.info("Returning user with id = " + id);
            return users.get(id);
        } else {
            throw new UserDoNotExistsException(String.join(" ",
                    "Пользователя с id", id.toString(), "не существует."));
        }
    }

    @Override
    public void delete(Integer id) {
        if (users.containsKey(id)) {
            log.info("Deleting user with id = " + id);
            emails.remove(users.get(id).getEmail());
            users.remove(id);
        } else {
            throw new UserDoNotExistsException(String.join(" ",
                    "Пользователя с id", id.toString(), "не существует."));
        }
    }
}
