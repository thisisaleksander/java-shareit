package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.model.ItemWithBooking;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    public Collection<ItemWithBooking> getItemsBy(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                                                  @RequestParam(value = "from", defaultValue = "0")@Min(0) int from,
                                                  @RequestParam(value = "size", defaultValue = "10")@Min(1) @Max(100) int size)
            throws ValidationException {
        log.info("[ItemController] -> get item request");
        return itemService.getItemsBy(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemWithBooking getById(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                                   @PathVariable long itemId) {
        log.info("[ItemController] -> get item id request");
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> findByText(@RequestParam(name = "text") String query,
                                    @RequestParam(value = "from", defaultValue = "0")@Min(0) int from,
                                    @RequestParam(value = "size", defaultValue = "10")@Min(1) @Max(100) int size)
            throws ValidationException {
        log.info("[ItemController] -> get item by text request");
        return itemService.findByText(query, from, size);
    }

    @PostMapping
    public Item add(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                    @RequestBody @Valid ItemDto itemDto) throws ValidationException {
        log.info("[ItemController] -> create new item request");
        return itemService.add(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public Comment addComment(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                              @RequestBody @Valid Comment comment,
                              @PathVariable long itemId) throws ValidationException {
        log.info("[ItemController] -> add new comment request");
        return itemService.addComment(userId, comment, itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                          @RequestBody ItemDto itemDto,
                          @PathVariable long itemId) {
        log.info("[ItemController] -> update item request");
        return itemService.update(userId, itemDto, itemId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                           @PathVariable long itemId) {
        log.info("[ItemController] -> delete item request");
        itemService.delete(userId, itemId);
    }
}