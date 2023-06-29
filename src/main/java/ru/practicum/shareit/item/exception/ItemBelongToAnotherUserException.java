package ru.practicum.shareit.item.exception;

public class ItemBelongToAnotherUserException extends RuntimeException {
    public ItemBelongToAnotherUserException(String message) {
        super(message);
    }
}
