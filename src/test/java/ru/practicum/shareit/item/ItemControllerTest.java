package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.item.mapper.ItemMapper.mapToItemDto;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Mock
    ItemService itemService;
    @InjectMocks
    ItemController itemController;

    @Test
    void getTest() throws ValidationException {
        ItemWithBooking itemWithBooking = new ItemWithBooking();
        List<ItemWithBooking> itemWithBookings = List.of(itemWithBooking);
        when(itemService.getItemsBy(1L, 1, 10)).thenReturn(itemWithBookings);

        List<ItemWithBooking> response = (List<ItemWithBooking>) itemController.getItemsBy(1L, 1, 10);

        assertEquals(itemWithBookings, response);
    }

    @Test
    void getByIdTest() {
        ItemWithBooking itemWithBooking = new ItemWithBooking();
        when(itemService.getItemById(1L, 1L)).thenReturn(itemWithBooking);

        ItemWithBooking response = itemController.getById(1L, 1L);

        assertEquals(itemWithBooking, response);

    }

    @Test
    void testGetByIdTest() throws ValidationException {
        ItemDto itemDto = new ItemDto();
        List<ItemDto> itemDtos = List.of(itemDto);
        when(itemService.findByText("good", 1, 10)).thenReturn(itemDtos);

        List<ItemDto> response = itemController.findByText("good", 1, 10);

        assertEquals(itemDtos, response);
    }

    @Test
    void addTest() throws ValidationException {

        Item item = new Item();
        when(itemService.add(1L, mapToItemDto(item))).thenReturn(item);

        Item response = itemController.add(1L, mapToItemDto(item));

        assertEquals(item, response);
    }

    @Test
    void addCommentTest() throws ValidationException {
        Comment comment = new Comment();
        when(itemService.addComment(1L, comment, 1L)).thenReturn(comment);

        Comment response = itemController.addComment(1L, comment, 1L);

        assertEquals(comment, response);
    }

    @Test
    void updateTest() {
        ItemDto itemDto = new ItemDto();
        when(itemService.update(1L, itemDto, 1L)).thenReturn(itemDto);

        ItemDto response = itemController.update(1L, itemDto, 1L);

        assertEquals(itemDto, response);
    }

    @Test
    void deleteItemTest() {
        itemController.deleteItem(1L, 1L);

        verify(itemService).delete(1L, 1L);
    }
}