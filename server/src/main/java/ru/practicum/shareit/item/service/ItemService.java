package ru.practicum.shareit.item.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.ItemWithBooking;

import java.util.Collection;
import java.util.List;

@Transactional(readOnly = true)
public interface ItemService {
    @Transactional
    Item add(long userId, ItemDto itemDto);

    Collection<ItemWithBooking> getItems(long userId, int from, int size);

    ItemWithBooking getItemById(long userId, long itemId);

    Item getItemById(long itemId);

    @Transactional
    ItemDto update(long userId, ItemDto itemDto, long itemId);

    @Transactional
    void delete(long userId, long itemId);

    @Transactional
    Comment addComment(long userId, Comment comment, long itemId);

    List<ItemDto> findByText(String query, int from, int size);
}