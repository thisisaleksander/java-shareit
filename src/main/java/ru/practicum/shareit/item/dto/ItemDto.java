package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
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
