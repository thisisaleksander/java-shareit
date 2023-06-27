package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemDto {
    private Integer id;
    @Size(max = 50)
    private String name;
    @Size(max = 250)
    private String description;
    private Boolean available;
    private User owner;

    public Boolean isAvailable() {
        return available;
    }
}
