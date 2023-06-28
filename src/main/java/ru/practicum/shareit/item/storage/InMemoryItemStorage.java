package ru.practicum.shareit.item.storage;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.exception.ItemBelongToAnotherUserException;
import ru.practicum.shareit.item.exception.ItemNotFountException;
import ru.practicum.shareit.user.User;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Slf4j
@Repository
public class InMemoryItemStorage implements ItemStorage {
    private Integer localId = 0;
    private final Map<Integer, Item> items = new HashMap<>();
    private final Map<Integer, Set<Integer>> userBelongings = new HashMap<>();

    private Integer getNewId() {
        return ++localId;
    }

    @Override
    public Item save(Item item) {
        log.info("[InMemoryItemStorage] -> save new item");
        item.setId(getNewId());
        items.put(item.getId(), item);
        log.info("[InMemoryItemStorage] -> new item saved with id = {}", item.getId());
        Integer userId = item.getOwner().getId();
        if (userBelongings.containsKey(userId)) {
            userBelongings.get(userId).add(item.getId());
        } else {
            Set<Integer> itemsIds = new HashSet<>();
            itemsIds.add(item.getId());
            log.info("[InMemoryItemStorage] -> created new list of items to user with id = {}", userId);
            userBelongings.put(userId, itemsIds);
        }
        if (isNull(item.isAvailable())) {
            items.get(item.getId()).setAvailable(true);
        }
        log.info("[InMemoryItemStorage] -> new item added to user with id = {}", userId);
        return items.get(item.getId());
    }

    @Override
    public Item update(Item item, Integer itemId, @NonNull User user) {
        if (isNull(userBelongings.get(user.getId())) || !userBelongings.get(user.getId()).contains(itemId)) {
            throw new ItemBelongToAnotherUserException("[InMemoryItemStorage] -> Item belongs to another user");
        }
        if (!items.containsKey(itemId)) {
            throw new ItemNotFountException(String.join(" ",
                    "[InMemoryItemStorage] -> item with id", itemId.toString(), "not found"));
        }
        if (!isNull(item.getName())) {
            if (!item.getName().isEmpty()) {
                log.info("[InMemoryItemStorage] -> item name updated, id = {}", itemId);
                items.get(itemId).setName(item.getName());
            }
        }
        if (!isNull(item.getDescription())) {
            if (!item.getDescription().isEmpty()) {
                log.info("[InMemoryItemStorage] -> item description updated, id = {}", itemId);
                items.get(itemId).setDescription(item.getDescription());
            }
        }
        if (!isNull(item.isAvailable())) {
            log.info("[InMemoryItemStorage] -> item description updated, id = {}", itemId);
            items.get(itemId).setAvailable(item.isAvailable());
        }
        log.info("[InMemoryItemStorage] -> data of item with id {} updated", itemId);
        return items.get(itemId);
    }

    @Override
    public Item getBy(Integer itemId) {
        if (items.containsKey(itemId)) {
            log.info("[InMemoryItemStorage] -> returning item with id = {}", itemId);
            return items.get(itemId);
        } else {
            throw new ItemNotFountException(String.join(" ",
                    "[InMemoryItemStorage] -> item with id", itemId.toString(), "not found"));
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
            log.info("[InMemoryItemStorage] -> {} items of user {} found", tmpItems.size(), user.getId());
            return tmpItems;
        } else {
            log.info("[InMemoryItemStorage] -> items of user with id {} not found", user.getId());
            return Collections.emptySet();
        }
    }

    @Override
    public List<Item> getBy(String text) {
        log.info("[InMemoryItemStorage] -> searching items by key word");
        return items.values().stream()
                .filter(Item::isAvailable)
                .filter((Item item) -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .sorted(Comparator.comparing(Item::getId))
                .collect(Collectors.toList());
    }
}
