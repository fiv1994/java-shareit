package ru.practicum.shareit.user.service.validation;

public class RequestParamIncorrectOrAbsentException extends RuntimeException {
    public RequestParamIncorrectOrAbsentException(String msg) {
        super(msg);
    }
}
