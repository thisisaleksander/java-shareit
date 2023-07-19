package ru.practicum.shareit.error.exception;

public class ItemRequestNotFoundException  extends NotFoundException {
    public ItemRequestNotFoundException(long bookingId) {
        super(String.format("ItemRequest with id %d not found", bookingId));
    }
}