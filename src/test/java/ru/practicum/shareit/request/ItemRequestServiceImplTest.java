package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.service.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.request.mapper.ItemRequestMapper.mapToItemRequest;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    ItemRequestRepository repository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserService userService;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Test
    void addNewItemRequestTest() {
        ItemRequestDto requestDto = new ItemRequestDto();
        ItemRequest request = mapToItemRequest(1L, requestDto, null);
        request.toString();
        when(repository.save(any(ItemRequest.class))).thenReturn(request);

        ItemRequest actualRequest = itemRequestService.addNewItemRequest(1L, requestDto);

        assertEquals(request, actualRequest);
        verify(repository).save(any(ItemRequest.class));
    }

    @Test
    void getRequestsTest() {
        ItemRequest itemRequest = new ItemRequest();
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);
        List<ItemRequest> itemRequestWithItems = itemRequestService.mapToRequestWithItems(itemRequests);
        when(repository.findByUserId(1L)).thenReturn(itemRequests);

        List<ItemRequest> actualRequests = (List<ItemRequest>) itemRequestService.getRequests(1L);

        assertEquals(itemRequestWithItems, actualRequests);
    }

    @Test
    void getAllRequestTest() throws ValidationException {
        ItemRequest itemRequest = new ItemRequest();
        List<ItemRequest> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);
        int from = 1;
        int size = 10;
        int pageIndex = from / size;
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "created");
        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        Page<ItemRequest> itemRequestPage = new PageImpl<>(itemRequests, page, itemRequests.size());
        when(repository.findAll(any(Pageable.class))).thenReturn(itemRequestPage);

        List<ItemRequest> actualItemRequestList = itemRequestService.getAllRequest(1L, 1, 10);
        assertEquals(itemRequests, actualItemRequestList);

        assertThrows(ValidationException.class,
                () -> itemRequestService.getAllRequest(1L, -1, 10));
    }

    @Test
    void getRequestByIdTest() {
        ItemRequest itemRequest = new ItemRequest();
        Item item = new Item();
        List<Item> items = new ArrayList<>();
        items.add(item);
        when(repository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        itemRequest.setItems(items);

        ItemRequest actualItemRequest = itemRequestService.getRequestById(1L, 1L);

        assertEquals(itemRequest, actualItemRequest);
    }
}