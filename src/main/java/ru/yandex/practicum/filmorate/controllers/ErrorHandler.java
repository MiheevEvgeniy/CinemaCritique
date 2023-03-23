package ru.yandex.practicum.filmorate.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException e) {
        e.printStackTrace();
        return Map.of("error", "Validation exception" + Arrays.toString(e.getStackTrace()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFound(final NullPointerException e) {
        e.printStackTrace();
        return Map.of("error", "Object not found" + Arrays.toString(e.getStackTrace()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleInternalError(final RuntimeException e) {
        e.printStackTrace();
        return Map.of("error", "Unexpected error" + Arrays.toString(e.getStackTrace()));
    }
}
