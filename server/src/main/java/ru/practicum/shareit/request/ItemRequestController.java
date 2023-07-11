package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;
import java.util.List;

import static ru.practicum.shareit.constant.Constants.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ItemRequestController {
    ItemRequestService itemRequestService;

    @GetMapping
    public Collection<ItemRequest> getItemRequest(@RequestHeader(value = X_SHARER_USER_ID) long userId) {
        log.info("[ItemRequestController] -> get item request by user id");
        return itemRequestService.getRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequest getItemRequestById(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                                          @PathVariable long requestId) {
        log.info("[ItemRequestController] -> get item request by id");
        return itemRequestService.getRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequest> getAllItemRequest(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                                               @RequestParam(value = "from", defaultValue = "0") int from,
                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("[ItemRequestController] -> get all items requests");
        return itemRequestService.getAllRequest(userId, from, size);
    }

    @PostMapping
    public ItemRequest add(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                    @RequestBody ItemRequestDto requestDto) {
        log.info("[ItemRequestController] -> add items request");
        return itemRequestService.addNewItemRequest(userId, requestDto);
    }
}