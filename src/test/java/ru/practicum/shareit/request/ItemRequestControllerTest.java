package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.User;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.request.mapper.ItemRequestMapper.mapToItemRequest;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    @Mock
    ItemRequestService itemRequestService;
    @InjectMocks
    ItemRequestController requestController;

    @Test
    void getItemRequestTest() {
        ItemRequest itemRequest = new ItemRequest();
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);
        when(itemRequestService.getRequests(1L)).thenReturn(itemRequests);

        List<ItemRequest> response = (List<ItemRequest>) requestController.getItemRequest(1L);

        assertEquals(itemRequests, response);
    }

    @Test
    void getItemRequestByIdTest() {
        ItemRequest itemRequest = new ItemRequest();
        when(itemRequestService.getRequestById(1L, 1L)).thenReturn(itemRequest);

        ItemRequest response = requestController.getItemRequestById(1L, 1L);

        assertEquals(itemRequest, response);
    }

    @Test
    void getAllItemRequestTest() throws ValidationException {
        ItemRequest itemRequest = new ItemRequest();
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);
        when(itemRequestService.getAllRequest(1L, 1, 1)).thenReturn(itemRequests);

        List<ItemRequest> response = requestController.getAllItemRequest(1L, 1, 1);

        assertEquals(itemRequests, response);
    }

    @Test
    void addTest() throws ValidationException {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        Item item = new Item();
        List<Item> items = List.of(item);
        User user = new User();
        user.setId(1L);
        ItemRequest itemRequest = mapToItemRequest(1L, itemRequestDto, items);

        when(itemRequestService.addNewItemRequest(1L, itemRequestDto)).thenReturn(itemRequest);

        ItemRequest response = requestController.add(1L, itemRequestDto);

        assertEquals(itemRequest, response);
    }
}