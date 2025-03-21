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
        validateEmail(user.getEmail(), user.getId());
    }

    public void validatePatchedUser(User user) {
        long userId = user.getId();
        validateExists(userId);

        String userEmail = user.getEmail();
        if (userEmail != null) {
            validateEmail(userEmail, userId);
        }
    }

    public void validateExists(long id) {
        NotFoundException exc = new NotFoundException(String.format("User with id = %d not found.", id));
        throwExceptionIfTrue(!userRepository.existsById(id), exc);
    }

    //Валидация почты  пользователя. Если userId = 0, то считается, что проверяется почта нового пользователя,
    // иначе - существующего
    private void validateEmail(String email, long userId) {
        boolean condition;
        Optional<User> userFoundByMail = userRepository.findByEmail(email);
        if (userId == 0) {
            condition = userFoundByMail.isEmpty();
        } else {
            condition = userFoundByMail.isEmpty() || userFoundByMail.get().getId() == userId;
        }

        String errorMessage = "";
        RuntimeException exc = null;

        if (!condition) {
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
