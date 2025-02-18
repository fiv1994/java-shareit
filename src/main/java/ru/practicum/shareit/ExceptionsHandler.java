package ru.practicum.shareit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.user.service.validation.AccessDeniedException;
import ru.practicum.shareit.user.service.validation.EmailBusyException;
import ru.practicum.shareit.user.service.validation.NotFoundException;
import ru.practicum.shareit.user.service.validation.RequestParamIncorrectOrAbsentException;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionsHandler {
    static final String EXCEPTION_CAUGHT_MESSAGE = "Caught exception by controller exception handler";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public Map<String, String> handleBadRequestException(RequestParamIncorrectOrAbsentException exc) {
        log.warn(EXCEPTION_CAUGHT_MESSAGE, exc);
        return Map.of("error", exc.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public Map<String, String> handleNotFoundException(NotFoundException exc) {
        log.warn(EXCEPTION_CAUGHT_MESSAGE, exc);
        return Map.of("error", exc.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    public Map<String, String> handleAccessDeniedException(AccessDeniedException exc) {
        log.warn(EXCEPTION_CAUGHT_MESSAGE, exc);
        return Map.of("error", exc.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public Map<String, String> handleEmailBusyException(EmailBusyException exc) {
        log.warn(EXCEPTION_CAUGHT_MESSAGE, exc);
        return Map.of("error", exc.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public Map<String, String> handleOtherExceptions(Throwable exc) {
        log.warn(EXCEPTION_CAUGHT_MESSAGE, exc);
        return Map.of("error", "Произошла ошибка на сервере.");
    }
}
