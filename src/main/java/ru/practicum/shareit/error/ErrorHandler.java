package ru.practicum.shareit.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.exception.InvalidArgumentException;
import ru.practicum.shareit.item.exception.ItemBelongToAnotherUserException;
import ru.practicum.shareit.item.exception.ItemDoNotExistsException;
import ru.practicum.shareit.user.exception.*;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleNotFoundException(final InvalidUserEmailException exception) {
        return new Error(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(final UserDoNotExistsException exception) {
        return new Error(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error handleNotFoundException(final UserEmailAlreadyExistsException exception) {
        return new Error(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(final ItemDoNotExistsException exception) {
        return new Error(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleNotFoundException(final InvalidArgumentException exception) {
        return new Error(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(final ItemBelongToAnotherUserException exception) {
        return new Error(exception.getMessage());
    }
}
