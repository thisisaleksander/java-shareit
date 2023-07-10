package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.error.exception.ItemRequestNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemRequestNotFoundExceptionTest {

    @Test
    public void testConstructor() {
        long bookingId = 123;
        ItemRequestNotFoundException exception = new ItemRequestNotFoundException(bookingId);

        String expectedMessage = String.format("ItemRequest with id %d not found", bookingId);
        assertEquals(expectedMessage, exception.getMessage());
    }
}