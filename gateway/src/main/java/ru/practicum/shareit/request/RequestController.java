package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;
    static final String USERID = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getItemRequest(@RequestHeader(USERID) long userId) {
        log.info("Get requests userId={}", userId);
        return requestClient.getRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(USERID) long userId,
                                                     @PathVariable long requestId) {
        log.info("Get requestsId= {}, userId={}", requestId, userId);
        return requestClient.getRequestById(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequest(@RequestHeader(USERID) long userId,
                                                    @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                                    @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        return requestClient.getAllRequest(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader(USERID) long userId,
                                       @RequestBody @Valid RequestDto requestDto) {
        return requestClient.addNewItemRequest(userId, requestDto);
    }
}
