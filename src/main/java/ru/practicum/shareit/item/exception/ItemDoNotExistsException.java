package ru.practicum.shareit.item.exception;

public class ItemDoNotExistsException extends RuntimeException {
    public ItemDoNotExistsException(String message) {
        super(message);
    }
}
