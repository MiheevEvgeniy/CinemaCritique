package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException e) {
        log.error("400 {}", e.getMessage());
        return new ErrorResponse("Validation exception " + e.getMessage(), e.getStackTrace());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFound(final NullPointerException e) {
        log.error("404 {}", e.getMessage());
        return new ErrorResponse("Object not found " + e.getMessage(), e.getStackTrace());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalError(final RuntimeException e) {
        log.error("500 {}", e.getMessage());
        return new ErrorResponse("Unexpected error " + e.getMessage(), e.getStackTrace());
    }

    private static class ErrorResponse {
        String error;
        StackTraceElement[] stackTrace;

        ErrorResponse(String error, StackTraceElement[] stackTrace) {
            this.error = error;
            this.stackTrace = stackTrace;
        }

        public String getError() {
            return error;
        }

        public StackTraceElement[] getStackTrace() {
            return stackTrace;
        }
    }
}

