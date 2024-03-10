package ru.practicum.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class ErrorResponse {

    private final HttpStatus status;
    private final String reason;
    private final String message;
    private final LocalDateTime timestamp;
}
