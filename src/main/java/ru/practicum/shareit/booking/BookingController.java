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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.shareit.constant.Constants.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BookingController {
    BookingService bookingService;

    @GetMapping("/{bookingId}")
    public Booking getById(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                           @PathVariable long bookingId) {
        log.info("[BookingController] -> bet booking by id request");
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<Booking> getByUserId(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                                     @RequestParam(defaultValue = "ALL") String state,
                                     @RequestParam(value = "from", defaultValue = "0")@Min(0) int from,
                                     @RequestParam(value = "size", defaultValue = "10")@Min(1) @Max(100) int size)
            throws ValidationException {
        log.info("[BookingController] -> bet booking by userId request");
        return bookingService.getByUserId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<Booking> getByOwnerId(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                                      @RequestParam(defaultValue = "ALL") String state,
                                      @RequestParam(value = "from", defaultValue = "0")@Min(0) int from,
                                      @RequestParam(value = "size", defaultValue = "10")@Min(1) @Max(100) int size)
            throws ValidationException {
        log.info("[BookingController] -> bet booking by owner request");
        return bookingService.getByOwnerId(userId, state, from, size);
    }

    @PatchMapping("/{bookingId}")
    public Booking approveBooking(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                                  @PathVariable long bookingId,
                                  @RequestParam boolean approved) throws ValidationException {
        log.info("[BookingController] -> approve booking request");
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @PostMapping
    public Booking add(@RequestHeader(value = X_SHARER_USER_ID) long userId,
                       @RequestBody @Valid BookingDto bookingDto) throws ValidationException {
        log.info("[BookingController] -> create new booking request");
        return bookingService.addNewBooking(userId, bookingDto);
    }
}
