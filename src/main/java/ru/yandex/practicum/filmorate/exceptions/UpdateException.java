package ru.yandex.practicum.filmorate.exceptions;

public class UpdateException extends RuntimeException {
    public UpdateException() {
    }

    public UpdateException(final String message) {
        super(message);
    }

    public UpdateException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UpdateException(final Throwable cause) {
        super(cause);
    }
}
