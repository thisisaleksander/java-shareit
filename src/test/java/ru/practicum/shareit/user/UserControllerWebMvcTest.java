package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserControllerWebMvcTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private BookingService bookingService;
    @MockBean
    private ItemService itemService;
    @MockBean
    private ItemRequestService itemRequestService;

    @SneakyThrows
    @Test
    void getAllUsersTest() {
        List<User> users = List.of(new User());
        when(userService.getAll()).thenReturn(users);

        String response = mockMvc.perform(get("/users"))

                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(users), response);
    }

    @SneakyThrows
    @Test
    void saveNewUserTest() {
        User user = new User();
        user.setEmail("i@i.ru");
        user.setName("i");
        when(userService.save(any(User.class))).thenReturn(user);

        String response = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(user), response);
    }

    @SneakyThrows
    @Test
    void saveNewUserTestShouldThrowExceptionEmail() {
        User user = new User();
        user.setEmail("i@i.ru");
        user.setName(null);
        when(userService.save(any(User.class))).thenReturn(user);

        String response = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService, never()).save(any(User.class));

    }

    @SneakyThrows
    @Test
    void saveNewUserTestShouldThrowExceptionName() {
        User user = new User();
        user.setEmail(null);
        user.setName("i");
        when(userService.save(any(User.class))).thenReturn(user);

        String response = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService, never()).save(any(User.class));

    }

    @SneakyThrows
    @Test
    void updateTest() {
        User user = new User();
        user.setId(1L);
        when(userService.update(anyLong(), any(User.class))).thenReturn(user);
        String response = mockMvc.perform(patch("/users/{userId}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(user), response);
    }

    @Test
    void updateTestShouldThrowException() throws Exception {
        User user = new User();
        user.setId(1L);
        doThrow(ValidationException.class).when(userService).update(anyLong(), any(User.class));

        mockMvc.perform(patch("/users/{userId}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @SneakyThrows
    @Test
    void getByIdTest() {
        User user = new User();
        user.setId(1L);
        user.setEmail("i@i.ru");
        user.setName("i");
        when(userService.getByUserId(1L)).thenReturn(user);
        String response = mockMvc.perform(get("/users/{userId}", user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(user), response);
    }

    @SneakyThrows
    @Test
    void deleteTest() {
        mockMvc.perform(delete("/users/{userId}", 1L))
                .andExpect(status().isOk());

        verify(userService).delete(1L);
    }
}