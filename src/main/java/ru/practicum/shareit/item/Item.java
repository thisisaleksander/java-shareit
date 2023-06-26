package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Item {
    @NonNull
    private Integer id;
    @NonNull
    private String name;
    private String description;
    @NonNull
    private boolean available;
    @NonNull
    private User owner;
    private ItemRequest request;

    public boolean isAvailable() {
        return available;
    }
}
