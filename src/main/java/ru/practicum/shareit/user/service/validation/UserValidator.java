package ru.practicum.shareit.user.service.validation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserValidator {
    private final UserRepository userRepository;

    public void validateNewUser(User user) {
        validateName(user.getName());
        validateEmail(user.getEmail(), 0);
    }

    public void validatePatchedUser(User user) {
        long userId = user.getId();
        validateExists(userId);

        String userName = user.getName();
        if (userName != null) {
            validateName(user.getName());
        }

        String userEmail = user.getEmail();
        if (userEmail != null) {
            validateEmail(userEmail, userId);
        }
    }

    public void validateExists(long id) {
        NotFoundException exc = new NotFoundException(String.format("User with id = %d not found.", id));
        throwExceptionIfTrue(!userRepository.existsById(id), exc);
    }

    private void validateName(String userName) {
        RequestParamIncorrectOrAbsentException exc =
                new RequestParamIncorrectOrAbsentException("Name can not be empty or contain only whitespaces.");
        throwExceptionIfTrue(isStringEmptyInJson(userName), exc);
    }

    // Валидация почты пользователя. Если userId = 0, то считается, что проверяется почта нового пользователя,
    // иначе - существующего
    private void validateEmail(String email, long userId) {
        //Проверка, что поле с почтой в запросе не пустое
        boolean condition1 = !isStringEmptyInJson(email);

        //Проверка, что почта соответствует шаблону
        String emailRegExp = "[\\w\\.]+@[a-z0-9]+\\.[a-z][a-z]+";
        boolean condition2 = email.matches(emailRegExp);

        //Проверка, что почта не занята
        boolean condition3;
        Optional<User> userFoundByMail = userRepository.findByEmail(email);
        if (userId == 0) {
            condition3 = userFoundByMail.isEmpty();
        } else {
            condition3 = userFoundByMail.isEmpty() || userFoundByMail.get().getId() == userId;
        }

        String errorMessage = "";
        RuntimeException exc = null;
        if (!condition1) {
            errorMessage = "Email is empty or absent.";
            exc = new RequestParamIncorrectOrAbsentException(errorMessage);
        } else if (!condition2) {
            errorMessage = "Email doesn't match template.";
            exc = new RequestParamIncorrectOrAbsentException(errorMessage);
        } else if (!condition3) {
            errorMessage = "Email is busy";
            exc = new EmailBusyException(errorMessage);
        }

        throwExceptionIfTrue(!errorMessage.isBlank(), exc);
    }

    public static void throwExceptionIfTrue(boolean condition, RuntimeException exc) {
        if (condition) {
            log.warn(exc.getMessage(), exc);
            throw exc;
        }
    }

    public static boolean isStringEmptyInJson(String string) {
        return string == null || string.isBlank();
    }


}