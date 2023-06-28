package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Set;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto create(@RequestBody @Valid UserDto userDto) {
        log.info("[UserController] -> create user request");
        return userService.create(userDto);
    }

    @GetMapping
    public Set<UserDto> getAll() {
        log.info("[UserController] -> get all users request");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserDto getBy(@PathVariable(value = "id") Integer userId) {
        log.info("[UserController] -> get user by id request");
        return userService.getBy(userId);
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody @Valid UserDto userDto,
                          @PathVariable(value = "id", required = false) Integer userId) {
        log.info("[UserController] -> update user request");
        return userService.update(userDto, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value = "id") Integer userId) {
        log.info("[UserController] -> delete user request");
        userService.delete(userId);
    }
}
