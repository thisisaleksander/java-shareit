package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.InvalidArgumentException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static ru.practicum.shareit.item.mapper.ItemMapper.toDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.toItem;

@Slf4j
@Service
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemService(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    public ItemDto create(ItemDto itemDto, Integer userId) {
        if (isNull(itemDto.getDescription()) || isNull(itemDto.getName())
                || itemDto.getName().isEmpty() || itemDto.getDescription().isEmpty()
                || isNull(itemDto.isAvailable()) || isNull(userId)) {
            throw new InvalidArgumentException("Переданы не все параметры для записи новой вещи.");
        }
        log.info("Mapping userDto to user, user id = " + itemDto.getId());
        User user = userStorage.getBy(userId);
        Item item = toItem(itemDto, user);
        return toDto(itemStorage.save(item));
    }

    public ItemDto update(ItemDto itemDto, Integer itemId, Integer userId) {
        if (isNull(userId) || isNull(itemId) || isNull(itemDto)) {
            throw new InvalidArgumentException("Переданы не все параметры для записи новой вещи.");
        }
        User user = userStorage.getBy(userId);
        Item item = toItem(itemDto, user);
        return toDto(itemStorage.update(item, itemId, user));
    }

    public ItemDto getBy(Integer itemId) {
        return toDto(itemStorage.getBy(itemId));
    }

    public Set<ItemDto> findBy(Integer userId) {
        User user = userStorage.getBy(userId);
        return itemStorage.getBy(user).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toSet());
    }

    public List<ItemDto> findBy(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemStorage.getBy(text).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }
}
