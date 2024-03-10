package ru.practicum.error;

public class BadRequestException extends RuntimeException {

    private final String message;

    public BadRequestException(String msg) {
        super(msg);
        this.message = msg;
    }
}
