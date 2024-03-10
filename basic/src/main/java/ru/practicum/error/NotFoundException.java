package ru.practicum.error;

public class NotFoundException extends RuntimeException {

    private final String message;

    public NotFoundException(String msg) {
        super(msg);
        this.message = msg;
    }
}
