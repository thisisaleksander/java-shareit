package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.comment.CommentRequest;

import ru.practicum.shareit.item.dto.ItemRequestDto;


import javax.validation.Valid;
import javax.validation.constraints.*;

import static ru.practicum.shareit.constant.Constants.X_SHARER_USER_ID;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> get(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                                      @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                      @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Get items userId={}, from={}, size={}", userId, from, size);

        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                                          @PathVariable long itemId) {
        log.info("Get itemId {}, userId={}", itemId, userId);
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getByQuery(@RequestParam(name = "text") String query,
                                             @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                             @Positive @RequestParam(value = "size", defaultValue = "10") int size) {

        return itemClient.getItemByQuery(query, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                                      @RequestBody @Valid ItemRequestDto itemRequestDto) {
        return itemClient.addNewItem(userId, itemRequestDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                                             @RequestBody @Valid CommentRequest commentRequest,
                                             @PathVariable long itemId) {
        return itemClient.addNewComment(userId, commentRequest, itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                                         @RequestBody ItemRequestDto itemRequestDto,
                                         @PathVariable long itemId) {
        return itemClient.updateItem(userId, itemRequestDto, itemId);
    }
}