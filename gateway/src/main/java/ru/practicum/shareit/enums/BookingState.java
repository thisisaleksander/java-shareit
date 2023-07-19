package ru.practicum.shareit.enums;

import ru.practicum.shareit.error.exception.ValidationException;

public enum BookingState {
	ALL,
	CURRENT,
	FUTURE,
	PAST,
	REJECTED,
	WAITING;

	public static BookingState from(String stringState) {
		for (BookingState state : values()) {
			if (state.name().equalsIgnoreCase(stringState)) {
				return state;
			}
		}
		throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
	}
}
