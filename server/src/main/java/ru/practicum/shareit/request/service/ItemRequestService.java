package ru.practicum.shareit.request.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;
import java.util.List;

@Transactional(readOnly = true)
public interface ItemRequestService {
    @Transactional
    ItemRequest addNewItemRequest(long userId, ItemRequestDto requestDto);

    Collection<ItemRequest> getRequests(long userId);

    List<ItemRequest> getAllRequest(long userId, long from, long size);

    ItemRequest getRequestById(long userId, long requestId);
}
