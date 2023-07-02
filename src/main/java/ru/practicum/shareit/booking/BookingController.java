package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.error.exception.ValidationException;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.constant.Constants.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BookingController {
    BookingService bookingService;

    @GetMapping("/{bookingId}")
    public Booking getById(@RequestHeader(X_SHARER_USER_ID) Integer userId,
                           @PathVariable Integer bookingId) throws ValidationException {
        log.info("[BookingController] -> get booking by id request");
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<Booking> getByUserId(@RequestHeader(X_SHARER_USER_ID) Integer userId,
                                     @RequestParam(defaultValue = "ALL") String state) throws ValidationException {
        log.info("[BookingController] -> get bookings by user id request");
        return bookingService.getByUserId(userId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getByOwnerId(@RequestHeader(X_SHARER_USER_ID) Integer userId,
                                      @RequestParam(defaultValue = "ALL") String state) throws ValidationException {
        log.info("[BookingController] -> get bookings by owner id request");
        return bookingService.getByOwnerId(userId, state);
    }

    @PatchMapping("/{bookingId}")
    public Booking approve(@RequestHeader(X_SHARER_USER_ID) Integer userId,
                           @PathVariable Integer bookingId,
                           @RequestParam Boolean approved) throws ValidationException {
        log.info("[BookingController] -> approve booking request");
        return bookingService.approve(userId, bookingId, approved);
    }

    @PostMapping
    public Booking add(@RequestHeader(X_SHARER_USER_ID) Integer userId,
                       @RequestBody @Valid BookingDto bookingDto)
            throws ValidationException {
        log.info("[BookingController] -> add booking request");
        return bookingService.add(userId, bookingDto);
    }
}
