package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAll() {
        log.info("[UserController] -> get all users request");
        return userService.getAll();
    }

    @PostMapping
    public User save(@RequestBody User user) {
        log.info("[UserController] -> create user request");
        return userService.save(user);
    }

    @PatchMapping("/{userId}")
    public User update(@RequestBody User user, @PathVariable long userId) {
        log.info("[UserController] -> update user request");
        return userService.update(userId, user);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable long userId) {
        log.info("[UserController] -> get user by id request");
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        log.info("[UserController] -> delete user request");
        userService.delete(userId);
    }
}