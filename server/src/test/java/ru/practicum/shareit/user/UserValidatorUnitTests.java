package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.validation.NotFoundException;
import ru.practicum.shareit.user.service.validation.UserValidator;

@SpringBootTest
public class UserValidatorUnitTests {

    @MockBean
    private UserRepository userRepository;
    @Autowired
    private UserValidator userValidator;

    @Test
    public void checkValidateExistsThrowsExceptionWhenUserIsAbsent() {
        Mockito.when(userRepository.existsById(3L))
                .thenReturn(false);
        Assertions.assertThrows(NotFoundException.class, () -> userValidator.validateExists(3L));
    }

    @Test
    public void checkValidateExistsDoesNotThrowExceptionWhenUserPresents() {
        Mockito.when(userRepository.existsById(3L))
                .thenReturn(true);
        Assertions.assertDoesNotThrow(() -> userValidator.validateExists(3L));
    }

    @Test
    public void checkValidateNewUserDoesNotThrowExceptionWhenDataIsCorrect() {
        User newUser = new User(0, "Serg", "biba.boba1995@mail.ru");
        Assertions.assertDoesNotThrow(() -> userValidator.validateNewUser(newUser));
    }

    @Test
    public void checkValidatePatchedUserDoesNotThrowExceptionWhenDataIsCorrect() {
        Mockito.when(userRepository.existsById(3L))
                .thenReturn(true);
        User user = new User(3, "Dan", "biba.boba1995@mail.ru");
        Assertions.assertDoesNotThrow(() -> userValidator.validatePatchedUser(user));
    }

    @Test
    public void checkValidatePatchedUserThrowsExceptionWhenUserIsAbsent() {
        Mockito.when(userRepository.existsById(3L))
                .thenReturn(false);
        User user = new User(3, "Dan", "biba.boba1995@mail.ru");
        Assertions.assertThrows(NotFoundException.class, () -> userValidator.validatePatchedUser(user));
    }
}
