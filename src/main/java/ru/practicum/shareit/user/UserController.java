package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserEmailAlreadyExistsException;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@RequestBody @Valid UserDto userDto) {
        log.info("[UserController] -> create user request");
        try {
            return userService.save(userDto);
        } catch (UserEmailAlreadyExistsException exception) {
        throw new RuntimeException(exception);
    }
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("[UserController] -> get all users request");
        return userService.getAll();
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto userDto, @PathVariable Integer userId) {
        log.info("[UserController] -> update user request");
        return userService.update(userId, userDto);
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable Integer userId) {
        log.info("[UserController] -> update user request");
        return userService.getBy(userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Integer userId) {
        log.info("[UserController] -> delete user request");
        userService.delete(userId);
    }
}