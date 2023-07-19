package ru.practicum.shareit.request.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.service.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.practicum.shareit.request.mapper.ItemRequestMapper.mapToItemRequest;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemRequestServiceImpl implements ItemRequestService {
    ItemRequestRepository itemRequestRepository;
    ItemRepository itemRepository;
    UserService userService;

    @Override
    public ItemRequest addNewItemRequest(long userId, ItemRequestDto requestDto) {
        userService.getUserById(userId);
        ItemRequest request = mapToItemRequest(userId, requestDto, null);
        return itemRequestRepository.save(request);
    }

    @Override
    public Collection<ItemRequest> getRequests(long userId) {
        validateUserId(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findByUserId(userId);
        return mapToRequestWithItems(itemRequests);
    }

    List<ItemRequest> mapToRequestWithItems(List<ItemRequest> itemRequests) {
        List<ItemRequest> itemRequestWithItems = new ArrayList<>();
        for (ItemRequest request: itemRequests) {
            List<Item> items = itemRepository.findByRequestId(request.getId()).orElse(null);
            request.setItems(items);
            itemRequestWithItems.add(request);
        }
        return itemRequestWithItems;
    }

    @Override
    public List<ItemRequest> getAllRequest(long userId, long from, long size) {
        if (from < 0) {
            throw new ValidationException("from is negative");
        }
        Sort sortByDate = Sort.by(Sort.Direction.ASC, "created");
        long pageIndex = from / size;
        Pageable page = PageRequest.of((int)pageIndex, (int) size, sortByDate);
        Page<ItemRequest> itemRequestPage = itemRequestRepository.findAll(page);
        List<ItemRequest> itemRequestList = itemRequestPage.toList();
        for (ItemRequest itemRequest : itemRequestList) {
            if (itemRequest.getUserId() == userId) {
                 if (itemRequestList.size() == 1) {
                     return new ArrayList<>();
                 }
                 itemRequestList.remove(itemRequest);
            }
        }
        return mapToRequestWithItems(itemRequestPage.toList());
    }

    @Override
    public ItemRequest getRequestById(long userId, long requestId) {
        validateUserId(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new ItemRequestNotFoundException(requestId));
        List<Item> items = itemRepository.findByRequestId(requestId).orElse(null);
        itemRequest.setItems(items);
        return itemRequest;
    }

    private void validateUserId(long userId) {
        userService.getUserById(userId);
    }
}
