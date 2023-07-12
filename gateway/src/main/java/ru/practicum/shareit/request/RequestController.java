package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.request.dto.CreateRequestDto;

import javax.validation.Valid;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.constant.Constants.X_SHARER_USER_ID;


@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @GetMapping
    public ResponseEntity<Object> getItemRequest(@RequestHeader(value = X_SHARER_USER_ID) long userId) {
        log.info("Get requests userId={}", userId);
        return requestClient.getRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                                                     @PathVariable long requestId) {
        log.info("Get requestsId= {}, userId={}", requestId, userId);
        return requestClient.getRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequest(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                                                    @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                                    @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        return requestClient.getAllRequest(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                                       @RequestBody @Valid CreateRequestDto requestDto) {
        return requestClient.addNewItemRequest(userId, requestDto);
    }
}
