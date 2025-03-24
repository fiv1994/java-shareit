package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.validation.NotFoundException;
import ru.practicum.shareit.user.service.validation.RequestParamIncorrectOrAbsentException;
import ru.practicum.shareit.user.service.validation.UserValidator;

import java.util.Optional;

@SpringBootTest
public class UserServiceUnitTests {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserValidator userValidator;

    @Autowired
    private UserService userService;

    @Test
    public void serviceCreatesNewUserWhenValidationPassed() {
        User expectedResult = new User(1L, "Bob", "Bob@java.org");
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(expectedResult);
        User result = userService.createUser(expectedResult.withId(0));

        boolean condition1 = result.getId() == expectedResult.getId();
        boolean condition2 = result.getName().equals(expectedResult.getName());
        boolean condition3 = result.getEmail().equals(expectedResult.getEmail());

        Assertions.assertTrue(condition1 && condition2 && condition3);
    }

    @Test
    public void testDoesNotCreateNewUserWhenValidationNotPassed() {
        User user = new User(0L, "   ", "Bob@java.org");
        RequestParamIncorrectOrAbsentException exc =
                new RequestParamIncorrectOrAbsentException("Name can not be empty or contain only whitespaces.");
        Mockito.doThrow(exc)
                .when(userValidator)
                .validateNewUser(Mockito.any(User.class));

        Assertions.assertThrows(RequestParamIncorrectOrAbsentException.class, () -> userService.createUser(user));
    }

    @Test
    public void serviceReturnsExistingUser() {
        long userId = 1L;
        User expectedResult = new User(userId, "Bob", "Bob@java.org");
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(expectedResult));
        User result = userService.getUser(userId);

        Assertions.assertEquals(result, expectedResult);
    }

    @Test
    public void serviceDoesNotReturnNotExistingUser() {
        Mockito.doThrow(new NotFoundException("User not Found"))
                .when(userValidator)
                .validateExists(Mockito.anyLong());

        Assertions.assertThrows(NotFoundException.class, () -> userService.getUser(1L));
    }

    @Test
    public void serviceDeletesExistingUser() {
        Assertions.assertDoesNotThrow(() -> userService.deleteUser(1L));
    }

    @Test
    public void serviceDoesNotDeleteNotExistingUser() {
        Mockito.doThrow(new NotFoundException("User not Found"))
                .when(userValidator)
                .validateExists(Mockito.anyLong());
        Assertions.assertThrows(NotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    public void servicePatchesUserWhenValidationPassed() {
        long id = 1L;
        String email = "Bob@java.org";
        User expectedResult = new User(id, "BobNew", email);

        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.of(new User(id, "Bob", email)));
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(expectedResult);

        User inputUser = new User(id, "BobNew", null);
        Assertions.assertEquals(expectedResult, userService.patchUser(inputUser));
    }

    @Test
    public void serviceDoesNotPatchAbsentUser() {
        Mockito.doThrow(new NotFoundException("User not Found"))
                .when(userValidator)
                .validatePatchedUser(Mockito.any(User.class));
        User user = new User(1L, "BobNew", "Bob@java.org");
        Assertions.assertThrows(NotFoundException.class, () -> userService.patchUser(user));
    }

    @Test
    public void serviceDoesNotPatchUserWhenDataIsIncorrect() {
        Mockito.doThrow(new RequestParamIncorrectOrAbsentException("Exc"))
                .when(userValidator)
                .validatePatchedUser(Mockito.any(User.class));
        User user = new User(1L, "   ", "Bob@java.org");
        Assertions.assertThrows(RequestParamIncorrectOrAbsentException.class, () -> userService.patchUser(user));
    }
}
