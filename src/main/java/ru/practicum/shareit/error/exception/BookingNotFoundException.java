package ru.practicum.shareit.error.exception;

public class BookingNotFoundException extends NotFoundException {
    public BookingNotFoundException(long bookingId) {
        super(String.format("Booking with id %d not found", bookingId));
    }
}
