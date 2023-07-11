package ru.practicum.shareit.booking.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import java.util.List;

@Transactional(readOnly = true)
public interface BookingService {

    Booking getBookingById(long userId, long bookingId);

    List<Booking> getByUserId(long userId, String state, int from, int size);

    List<Booking> getByOwnerId(long userId, String state, int from, int size);

    @Transactional
    Booking approveBooking(long userId, long bookingId, boolean approve);

    @Transactional
    Booking addNewBooking(long userId, BookingDto bookingDto);
}
