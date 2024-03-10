package ru.practicum.error;

public class ConflictException extends RuntimeException {

    private final String message;

    public ConflictException(String message) {
        super(message);
        this.message = message;
    }
}
