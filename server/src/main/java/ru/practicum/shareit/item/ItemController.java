package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.model.ItemWithBooking;
import java.util.Collection;
import java.util.List;

import static ru.practicum.shareit.constant.Constants.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemController {
    ItemService itemService;

    @GetMapping
    public Collection<ItemWithBooking> get(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                                           @RequestParam(value = "from", defaultValue = "0") int from,
                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("[ItemController] -> get item request");
        return itemService.getItems(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemWithBooking getById(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                           @PathVariable long itemId) {
        log.info("[ItemController] -> get item id request");
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> findByText(@RequestParam(name = "text") String query,
                                    @RequestParam(value = "from", defaultValue = "0") int from,
                                    @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("[ItemController] -> get item by text request");
        return itemService.findByText(query, from, size);
    }

    @PostMapping
    public Item add(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                    @RequestBody ItemDto itemDto) {
        log.info("[ItemController] -> create new item request");
        return itemService.add(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public Comment addComment(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                           @RequestBody Comment comment,
                           @PathVariable long itemId) {
        return itemService.addComment(userId, comment, itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                          @RequestBody ItemDto itemDto,
                          @PathVariable long itemId) {
        log.info("[ItemController] -> add new comment request");
        return itemService.update(userId, itemDto, itemId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                           @PathVariable long itemId) {
        itemService.delete(userId, itemId);
    }
}