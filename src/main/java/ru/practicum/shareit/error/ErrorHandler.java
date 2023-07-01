package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.item.exception.InvalidArgumentException;
import ru.practicum.shareit.item.exception.ItemBelongToAnotherUserException;
import ru.practicum.shareit.item.exception.ItemIsNotAvailableException;
import ru.practicum.shareit.item.exception.ItemNotFountException;
import ru.practicum.shareit.user.exception.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleNotFoundException(final InvalidUserEmailException exception) {
        log.info("400 {}", exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(final UserNotFoundException exception) {
        log.info("404 {}", exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error handleNotFoundException(final UserEmailAlreadyExistsException exception) {
        log.info("409 {}", exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(final ItemNotFountException exception) {
        log.info("404 {}", exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleNotFoundException(final InvalidArgumentException exception) {
        log.info("400 {}", exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(final ItemBelongToAnotherUserException exception) {
        log.info("404 {}", exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleNotFoundException(final ItemIsNotAvailableException exception) {
        log.info("404 {}", exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(final BookingAlreadyApprovedException exception) {
        log.info("404 {}", exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(final BookingNotFoundException exception) {
        log.info("404 {}", exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(final InvalidBookingException exception) {
        log.info("404 {}", exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(final InvalidOwnerException exception) {
        log.info("404 {}", exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(final InvalidStatusException exception) {
        log.info("404 {}", exception.getMessage(), exception);
        return new Error(exception.getMessage());
    }
}
