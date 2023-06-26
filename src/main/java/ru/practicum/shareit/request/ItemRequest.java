package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.User;

import java.util.Date;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemRequest {
    private Integer id;
    private String description;
    private User requestor;
    private Date created;
}
