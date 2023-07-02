package ru.practicum.shareit.item.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.model.ItemWithBooking;

import java.util.List;

@Transactional(readOnly = true)
public interface ItemService {
    Item add(Integer userId, ItemDto itemDto) throws ValidationException;

    List<ItemWithBooking> getByUserId(Integer userId);

    ItemWithBooking getByItemId(Integer userId, Integer itemId);

    Item getByItemId(Integer itemId);

    @Transactional
    ItemDto update(Integer userId, ItemDto itemDto, Integer itemId);

    @Transactional
    void delete(Integer userId, Integer itemId) throws ValidationException;

    @Transactional
    Comment comment(Integer userId, Comment comment, Integer itemId) throws ValidationException;

    List<ItemDto> findByText(String text);
}