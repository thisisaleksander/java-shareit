package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.error.exception.AlreadyExistException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlreadyExistExceptionTest {

    @Test
    public void testConstructor() {
        String message = "Item already exists";
        AlreadyExistException exception = new AlreadyExistException(message);

        assertEquals(message, exception.getMessage());
    }
}
