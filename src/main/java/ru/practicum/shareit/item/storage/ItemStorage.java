package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Set;

public interface ItemStorage {
    Item save(Item item);

    Item update(Item item, Integer itemId, User user);

    Item getBy(Integer itemId);

    Set<Item> getBy(User user);

    List<Item> getBy(String text);
}
