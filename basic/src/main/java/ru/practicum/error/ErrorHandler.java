package ru.practicum.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadArgumentException(final MethodArgumentNotValidException exc) { //DtoIn validate
        log.debug("Получен статус 400 Bad request {}", exc.getMessage(), exc);
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                Objects.requireNonNull(exc.getFieldError()).getDefaultMessage(),
                exc.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException exc) { // Element not found
        log.debug("Получен статус 404 Not found {}", exc.getMessage(), exc);
        return new ErrorResponse(
                HttpStatus.NOT_FOUND,
                exc.getMessage(),
                exc.getMessage(),
                LocalDateTime.now());
    }
}
