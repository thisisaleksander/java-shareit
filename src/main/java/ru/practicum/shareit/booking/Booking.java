package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.Date;

/**
 * TODO Sprint add-bookings.
 */

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Booking {
    private Integer id;
    private Date start;
    private Date end;
    private Item item;
    private User booker;
    private String status;
}
