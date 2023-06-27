package ru.practicum.shareit.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *  TODO Sprint add-controllers.
 */

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    private Integer id;
    @Size(min = 2, max = 30)
    private String name;
    @Email
    private String email;
}
