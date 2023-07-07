package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    UserService userService;
    @InjectMocks
    UserController userController;

    @Test
    void getAllUsersTest() {
        List<User> users = List.of(new User());
        when(userService.getAll()).thenReturn(users);

        List<User> response = userController.getAll();

        assertEquals(users, response);
    }

    @Test
    void saveNewUserTest() {
        User user = new User();
        when(userService.save(user)).thenReturn(user);

        User response = userController.save(user);

        assertEquals(user, response);
    }

    @Test
    void updateTest() throws ValidationException {
        User user = new User();
        user.setId(1L);
        when(userService.update(1L, user)).thenReturn(user);
        User response = userController.update(user, 1L);

        assertEquals(user, response);
    }

    @Test
    void getByIdTest() {
        User user = new User();
        user.setId(1L);
        when(userService.getByUserId(1L)).thenReturn(user);
        User response = userController.getByUserId(1L);

        assertEquals(user, response);
    }

    @Test
    void deleteTest() {
        userController.delete(1L);

        verify(userService).delete(anyLong());
    }
}