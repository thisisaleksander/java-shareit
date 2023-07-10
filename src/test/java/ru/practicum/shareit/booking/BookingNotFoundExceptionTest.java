package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.error.exception.BookingNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingNotFoundExceptionTest {

    @Test
    public void testConstructor() {
        long bookingId = 123;
        BookingNotFoundException exception = new BookingNotFoundException(bookingId);

        String expectedMessage = String.format("Booking with id %d not found", bookingId);
        assertEquals(expectedMessage, exception.getMessage());
    }
}
