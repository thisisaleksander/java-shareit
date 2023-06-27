package ru.practicum.shareit.item.storage;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.exception.InvalidArgumentException;
import ru.practicum.shareit.item.exception.ItemBelongToAnotherUserException;
import ru.practicum.shareit.item.exception.ItemDoNotExistsException;
import ru.practicum.shareit.user.User;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.isNull;

@Slf4j
@Repository
public class InMemoryItemStorage implements ItemStorage {
    private Integer localId;
    private final Map<Integer, Item> items = new HashMap<>();
    private final Map<Integer, Set<Integer>> userBelongings = new HashMap<>();

    private Integer getNewId() {
        if (localId == null) {
            localId = 0;
        }
        return ++localId;
    }

    @Override
    public Item save(Item item) {
        item.setId(getNewId());
        items.put(item.getId(), item);
        Integer userId = item.getOwner().getId();
        if (userBelongings.containsKey(userId)) {
            userBelongings.get(userId).add(item.getId());
        } else {
            Set<Integer> itemsIds = new HashSet<>();
            itemsIds.add(item.getId());
            userBelongings.put(userId, itemsIds);
        }
        if (isNull(item.isAvailable())) {
            items.get(item.getId()).setAvailable(true);
        }
        return items.get(item.getId());
    }

    @Override
    public Item update(Item item, Integer itemId, @NonNull User user) {
        Item originalItem = items.get(itemId);
        if (isNull(userBelongings.get(user.getId())) || !userBelongings.get(user.getId()).contains(itemId)) {
            throw new ItemBelongToAnotherUserException("Вещь принадлежит другому пользователю");
        }
        if (!items.containsKey(itemId)) {
            throw new ItemDoNotExistsException(String.join(" ",
                    "Вещь с id", itemId.toString(), "не найдена."));
        }
        if (!isNull(item.getName())) {
            if (!item.getName().isEmpty()) {
                log.info("Item name updated, id = " + itemId);
                items.get(itemId).setName(item.getName());
            }
        }
        if (!isNull(item.getDescription())) {
            if (!item.getDescription().isEmpty()) {
                log.info("Item description updated, id = " + itemId);
                items.get(itemId).setDescription(item.getDescription());
            }
        }
        if (!isNull(item.isAvailable())) {
            log.info("Item description updated, id = " + itemId);
            items.get(itemId).setAvailable(item.isAvailable());
        }
        return items.get(itemId);
    }

    @Override
    public Item getBy(Integer itemId) {
        if (items.containsKey(itemId)) {
            log.info("Returning item with id = " + itemId);
            return items.get(itemId);
        } else {
            throw new ItemDoNotExistsException(String.join(" ",
                    "Вещь с id", itemId.toString(), "не найдена."));
        }
    }

    @Override
    public Set<Item> getBy(User user) {
        if (userBelongings.containsKey(user.getId())) {
            Set<Integer> itemsIds = userBelongings.get(user.getId());
            Set<Item> tmpItems = new HashSet<>();
            for (Integer tmpItemId : itemsIds) {
                tmpItems.add(items.get(tmpItemId));
            }
            return tmpItems;
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public Set<Item> getBy(String text) {
        return items.values().stream()
                .filter(Item::isAvailable)
                .filter((Item item) -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toSet());
    }
}
