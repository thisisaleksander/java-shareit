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
import javax.validation.Valid;
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
    public List<ItemWithBooking> getByUserId(@RequestHeader(value = X_SHARER_USER_ID) Integer userId) {
        log.info("[ItemController] -> get item by user id request");
        return itemService.getByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemWithBooking getById(@RequestHeader(value = X_SHARER_USER_ID) Integer userId,
                                   @PathVariable Integer itemId) {
        log.info("[ItemController] -> get item by id request");
        return itemService.getByItemId(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> findByText(@RequestParam(name = "text") String text) {
        log.info("[ItemController] -> get items by key word request");
        return itemService.findByText(text);
    }

    @PostMapping
    public Item create(@RequestHeader(value = X_SHARER_USER_ID) Integer userId,
                       @RequestBody @Valid ItemDto itemDto) {
        log.info("[ItemController] -> create item request");
        return itemService.add(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public Comment comment(@RequestHeader(value = X_SHARER_USER_ID) Integer userId,
                           @RequestBody @Valid Comment comment,
                           @PathVariable Integer itemId) {
        log.info("[ItemController] -> add comment to item request");
        return itemService.comment(userId, comment, itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(value = X_SHARER_USER_ID) Integer userId,
                          @RequestBody ItemDto itemDto,
                          @PathVariable Integer itemId) {
        log.info("[ItemController] -> update item request");
        return itemService.update(userId, itemDto, itemId);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader(value = X_SHARER_USER_ID) Integer userId,
                       @PathVariable Integer itemId) {
        log.info("[ItemController] -> delete item request");
        itemService.delete(userId, itemId);
    }
}