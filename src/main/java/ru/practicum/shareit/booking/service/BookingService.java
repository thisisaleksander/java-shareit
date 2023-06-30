package ru.practicum.shareit.booking.service;


import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import java.util.List;

@Transactional(readOnly = true)
public interface BookingService {

    @Transactional(readOnly = true)
    Booking getBookingById(Integer userId, Integer bookingId);

    @Transactional(readOnly = true)
    List<Booking> getByUserId(Integer userId, String state);

    @Transactional(readOnly = true)
    List<Booking> getByOwnerId(Integer userId, String state);

    @Transactional
    Booking approve(Integer userId, Integer bookingId, Boolean approve);

    @Transactional
    Booking add(Integer userId, BookingDto bookingDto);
}
