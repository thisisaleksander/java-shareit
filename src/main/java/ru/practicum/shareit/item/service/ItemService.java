package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemDto create(ItemDto itemDto, Integer userId) {
        if (isNull(itemDto.getDescription()) || isNull(itemDto.getName())
                || itemDto.getName().isEmpty() || itemDto.getDescription().isEmpty()
                || isNull(itemDto.isAvailable()) || isNull(userId)) {
            throw new InvalidArgumentException("Missing arguments in request!");
        }
        log.info("[ItemService] -> create, item id = {}, user id = {}", itemDto.getId(), userId);
        User user = userStorage.getBy(userId);
        Item item = toItem(itemDto, user);
        return toDto(itemStorage.save(item));
    }

    public ItemDto update(ItemDto itemDto, Integer itemId, Integer userId) {
        if (isNull(userId) || isNull(itemId) || isNull(itemDto)) {
            throw new InvalidArgumentException("Missing arguments in request!");
        }
        log.info("[ItemService] -> update, item id = {}, user id = {}", itemId, userId);
        User user = userStorage.getBy(userId);
        Item item = toItem(itemDto, user);
        return toDto(itemStorage.update(item, itemId, user));
    }

    public ItemDto getBy(Integer itemId) {
        log.info("[ItemService] -> get item by id, id = {}", itemId);
        return toDto(itemStorage.getBy(itemId));
    }

    public Set<ItemDto> findBy(Integer userId) {
        log.info("[ItemService] -> get items by user, id = {}", userId);
        User user = userStorage.getBy(userId);
        return itemStorage.getBy(user).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toSet());
    }

    public List<ItemDto> findBy(String text) {
        log.info("[ItemService] -> find items by key word, text: {}", text);
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemStorage.getBy(text).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }
}
