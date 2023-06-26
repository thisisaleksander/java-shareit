package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 *  TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    private Integer id;
    private String name;
    private String email;
}
