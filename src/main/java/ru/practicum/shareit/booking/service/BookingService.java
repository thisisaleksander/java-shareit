package ru.practicum.shareit.booking.service;


import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.error.exception.ValidationException;
import java.util.List;

@Transactional(readOnly = true)
public interface BookingService {

    @Transactional(readOnly = true)
    Booking getBookingById(long userId, long bookingId);

    @Transactional(readOnly = true)
    List<Booking> getByUserId(long userId, String state) throws ValidationException;

    @Transactional(readOnly = true)
    List<Booking> getByOwnerId(long userId, String state) throws ValidationException;

    @Transactional
    Booking approveBooking(long userId, long bookingId, boolean approve) throws ValidationException;

    @Transactional
    Booking addNewBooking(long userId, BookingDto bookingDto) throws ValidationException;
}
