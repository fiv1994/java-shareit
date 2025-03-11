package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody User user) {
        log.info("Started request handling by UserController#createUser(...)");
        log.info("Started creating user");
        User createdUser = userService.createUser(user);
        log.info("User with id = {} created.", createdUser.getId());
        return UserDtoMapper.transformToDto(createdUser);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable long userId) {
        log.info("Started request handling by UserController#getUser(...)");
        log.info("Started extracting user with id = {}", userId);
        User requestedUser = userService.getUser(userId);
        log.info("User with id = {} extracted", userId);
        return UserDtoMapper.transformToDto(requestedUser);
    }

    @PatchMapping("/{userId}")
    public UserDto patchUser(@PathVariable("userId") String userIdString, @RequestBody User user) {
        log.info("Started request handling by UserController#patchUser(...)");
        user.setId(Long.parseLong(userIdString));
        log.info("Started updating user with id = {}", user.getId());
        User result = userService.patchUser(user);
        log.info("Ended updating user with id = {}", userIdString);

        return UserDtoMapper.transformToDto(result);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Started request handling by UserController#deleteUser(...)");
        log.info("Started deleting user with id = {}", userId);
        userService.deleteUser(userId);
        log.info("Ended deleting user with id = {}", userId);
    }
}
