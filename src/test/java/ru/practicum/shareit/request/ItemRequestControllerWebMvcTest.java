package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.request.mapper.ItemRequestMapper.mapToItemRequest;

@WebMvcTest
class ItemRequestControllerWebMvcTest {
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
    void getItemRequestTest() {
        ItemRequest itemRequest = new ItemRequest();
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);
        when(itemRequestService.getRequests(1L)).thenReturn(itemRequests);

        String response = mockMvc.perform(get("/requests", 1L)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequests), response);
    }

    @SneakyThrows
    @Test
    void getItemRequestByIdTest() {
        ItemRequest itemRequest = new ItemRequest();
        when(itemRequestService.getRequestById(1L, 1L)).thenReturn(itemRequest);

        String response = mockMvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequest), response);
    }

    @SneakyThrows
    @Test
    void getAllItemRequestTest() {
        ItemRequest itemRequest = new ItemRequest();
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);
        when(itemRequestService.getAllRequest(1L, 1, 10)).thenReturn(itemRequests);

        String response = mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequests), response);
    }

    @SneakyThrows
    @Test
    void addTest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("text");
        Item item = new Item();
        List<Item> items = List.of(item);
        User user = new User();
        user.setId(1L);
        ItemRequest itemRequest = mapToItemRequest(1L, itemRequestDto, items);

        when(itemRequestService.addNewItemRequest(1L, itemRequestDto)).thenReturn(itemRequest);

        String response = mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequest), response);
    }

    @SneakyThrows
    @Test
    void addTestShouldThrowException() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription(null);
        Item item = new Item();
        List<Item> items = List.of(item);
        User user = new User();
        user.setId(1L);
        ItemRequest itemRequest = mapToItemRequest(1L, itemRequestDto, items);

        when(itemRequestService.addNewItemRequest(1L, itemRequestDto)).thenReturn(itemRequest);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestService, never()).addNewItemRequest(anyLong(), any(ItemRequestDto.class));
    }
}