package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {
    long id;

    @NotNull(message = "email не может быть пустым")
    @Email(message = "email введен не верно")
    String email;

    @NotNull(message = "Имя не может быть пустым")
    @NotBlank(message = "Имя не может быть пустым или содержать только пробелы")
    String name;
}
