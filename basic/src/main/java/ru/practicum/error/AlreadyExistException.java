package ru.practicum.error;

public class AlreadyExistException  extends RuntimeException {

    private String message;

    public AlreadyExistException(String msg) {
        super(msg);
    }
}
