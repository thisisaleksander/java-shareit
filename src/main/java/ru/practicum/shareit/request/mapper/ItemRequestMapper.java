package ru.practicum.shareit.request.mapper;

import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
public class ItemRequestMapper {
    public static ItemRequest mapToItemRequest(long userId, ItemRequestDto dto, List<Item> items) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(dto.getId());
        itemRequest.setUserId(userId);
        itemRequest.setDescription(dto.getDescription());
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setItems(items);
        return itemRequest;
    }
}
