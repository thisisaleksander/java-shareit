package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {
    long id;
    @NotBlank(message = "Имя не может быть пустым или содержать только пробелы")
    String name;
    @NotBlank(message = "Описание не может быть пустым или содержать только пробелы")
    String description;
    long count;
    @AssertTrue
    Boolean available;
    long  requestId;
}
