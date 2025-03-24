package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.validation.NotFoundException;

@Transactional
@SpringBootTest
public class UserServiceIntegrationTests {

    @Autowired
    private UserService userService;

    @Test
    public void serviceCreatesNewUserWhenValidationPassed() {
        User expectedResult = new User(0, "Bob", "Bob@java.org");
        User result = userService.createUser(expectedResult.withId(0));

        boolean condition1 = result.getId() > 0;
        boolean condition2 = result.getName().equals(expectedResult.getName());
        boolean condition3 = result.getEmail().equals(expectedResult.getEmail());

        Assertions.assertTrue(condition1 && condition2 && condition3);
    }

    @Test
    public void serviceReturnsExistingUser() {
        User userToCreate = new User(0, "Bob", "Bob@java.org");
        long createdUserId = userService.createUser(userToCreate).getId();
        User result = userService.getUser(createdUserId);

        boolean condition1 = result.getName().equals(userToCreate.getName());
        boolean condition2 = result.getEmail().equals(userToCreate.getEmail());
        Assertions.assertTrue(condition1 && condition2);
    }

    @Test
    public void serviceDoesNotReturnNotExistingUser() {
        Assertions.assertThrows(NotFoundException.class, () -> userService.getUser(1L));
    }

    @Test
    public void serviceDeletesExistingUser() {
        User userToCreate = new User(0, "Bob", "Bob@java.org");
        long createdUserId = userService.createUser(userToCreate).getId();
        Assertions.assertDoesNotThrow(() -> userService.deleteUser(createdUserId));
    }

    @Test
    public void serviceDoesNotDeleteNotExistingUser() {
        Assertions.assertThrows(NotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    public void servicePatchesUserWhenValidationPassed() {
        User userToCreate = new User(0, "Bob", "Bob@java.org");
        long createdUserId = userService.createUser(userToCreate).getId();
        User patchedUser = new User(createdUserId, "James", null);
        userService.patchUser(patchedUser);
        User result = userService.getUser(createdUserId);
        Assertions.assertEquals(patchedUser.getName(), result.getName());
    }

    @Test
    public void serviceDoesNotPatchAbsentUser() {
        User patchedUser = new User(1L, "James", null);
        Assertions.assertThrows(NotFoundException.class, () -> userService.patchUser(patchedUser));
    }
}
