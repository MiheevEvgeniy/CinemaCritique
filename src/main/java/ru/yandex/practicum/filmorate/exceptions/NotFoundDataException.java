package ru.yandex.practicum.filmorate.exceptions;

public class NotFoundDataException extends RuntimeException {
    public NotFoundDataException() {
    }

    public NotFoundDataException(final String message) {
        super(message);
    }

    public NotFoundDataException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NotFoundDataException(final Throwable cause) {
        super(cause);
    }
}
