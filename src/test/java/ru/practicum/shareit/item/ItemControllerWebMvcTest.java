package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.item.mapper.ItemMapper.mapToItemDto;

@WebMvcTest
class ItemControllerWebMvcTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;
    @MockBean
    private UserService userService;
    @MockBean
    private BookingService bookingService;
    @MockBean
    private ItemRequestService itemRequestService;

    @SneakyThrows
    @Test
    void getTest() {
        ItemWithBooking itemWithBooking = new ItemWithBooking();
        List<ItemWithBooking> itemWithBookings = List.of(itemWithBooking);
        when(itemService.getItemsBy(1L, 1, 10)).thenReturn(itemWithBookings);

        String response = mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "1")
                        .param("size", "10"))

                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemWithBookings), response);
    }

    @SneakyThrows
    @Test
    void getByIdTest() {
        ItemWithBooking itemWithBooking = new ItemWithBooking();
        when(itemService.getItemById(1L, 1L)).thenReturn(itemWithBooking);

        String response = mockMvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemWithBooking), response);
    }

    @SneakyThrows
    @Test
    void testGetByIdTest() {
        ItemDto itemDto = new ItemDto();
        List<ItemDto> itemDtos = List.of(itemDto);
        when(itemService.findByText("good", 1, 10)).thenReturn(itemDtos);

        String response = mockMvc.perform(get("/items//search", 1L)
                        .param("text", "good")
                        .param("from", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDtos), response);
    }

    @SneakyThrows
    @Test
    void addTest() {
        Item item = new Item();
        item.setName("i");
        item.setDescription("I");
        item.setAvailable(true);
        when(itemService.add(1L, mapToItemDto(item))).thenReturn(item);

        String response = mockMvc.perform(post("/items", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(mapToItemDto(item))))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(item), response);
    }

    @SneakyThrows
    @Test
    void addTestShouldThrowExceptionName() {
        Item item = new Item();
        item.setName(null);
        item.setDescription("I");
        item.setAvailable(true);
        when(itemService.add(1L, mapToItemDto(item))).thenReturn(item);

        mockMvc.perform(post("/items", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(mapToItemDto(item))))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, never()).add(anyLong(), any(ItemDto.class));

    }

    @SneakyThrows
    @Test
    void addTestShouldThrowExceptionDescription() {
        Item item = new Item();
        item.setName("i");
        item.setDescription(null);
        item.setAvailable(true);
        when(itemService.add(1L, mapToItemDto(item))).thenReturn(item);

        mockMvc.perform(post("/items", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(mapToItemDto(item))))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, never()).add(anyLong(), any(ItemDto.class));

    }

    @SneakyThrows
    @Test
    void addTestShouldThrowExceptionAvailable() {
        Item item = new Item();
        item.setName("i");
        item.setDescription("I");
        item.setAvailable(false);
        when(itemService.add(1L, mapToItemDto(item))).thenReturn(item);

        mockMvc.perform(post("/items", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(mapToItemDto(item))))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, never()).add(anyLong(), any(ItemDto.class));

    }

    @SneakyThrows
    @Test
    void addCommentTest() {
        Comment comment = new Comment();
        comment.setText("text");
        when(itemService.addComment(1L, comment, 1L)).thenReturn(comment);

        String response = mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(comment), response);
    }

    @SneakyThrows
    @Test
    void addCommentTestShouldThrowException() {
        Comment comment = new Comment();
        comment.setText(null);
        when(itemService.addComment(1L, comment, 1L)).thenReturn(comment);

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, never()).addComment(anyLong(), any(Comment.class), anyLong());
    }

    @SneakyThrows
    @Test
    void updateTest() {
        ItemDto itemDto = new ItemDto();
        when(itemService.update(1L, itemDto, 1L)).thenReturn(itemDto);

        String response = mockMvc.perform(patch("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), response);
    }

    @SneakyThrows
    @Test
    void deleteItemTest() {
        mockMvc.perform(delete("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

        verify(itemService).delete(1L, 1L);
    }
}