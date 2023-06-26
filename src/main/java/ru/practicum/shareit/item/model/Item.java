package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Item {
    private Integer id;
    private String name;
    private String description;
    private String available;
    private String owner;
    private String request;
}
