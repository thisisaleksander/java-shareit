package ru.practicum.shareit.error.exception;

public class ItemNotFoundException extends NotFoundException {
    public ItemNotFoundException(long itemId) {
        super(String.format("Item with id %d not found", itemId));
    }
}
