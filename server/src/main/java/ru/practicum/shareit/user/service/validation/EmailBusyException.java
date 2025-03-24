package ru.practicum.shareit.user.service.validation;

public class EmailBusyException extends RuntimeException {
    public EmailBusyException(String msg) {
        super(msg);
    }
}
