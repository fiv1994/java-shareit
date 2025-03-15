package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.validation.UserValidator;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserValidator userValidator;
    private final UserRepository userRepository;

    public User createUser(User user) {
        log.debug("Launched UserService#createUser(...)");
        userValidator.validateNewUser(user);
        log.debug("Ended UserService#createUser(...)");
        return userRepository.save(user);
    }

    public User getUser(long id) {
        log.debug("Launched UserService#getUser(...)");
        userValidator.validateExists(id);
        log.debug("Ended UserService#getUser(...)");
        return userRepository.findById(id).get();
    }

    public User patchUser(User user) {
        log.debug("Launched UserService#patchUser(...)");

        userValidator.validatePatchedUser(user);

        long userId = user.getId();
        User userFromStorage = userRepository.findById(userId).get();
        if (user.getEmail() == null) {
            user.setEmail(userFromStorage.getEmail());
        }
        if (user.getName() == null) {
            user.setName(userFromStorage.getName());
        }

        log.debug("Ended UserService#patchUser(...)");
        return userRepository.save(user);
    }

    public void deleteUser(long id) {
        log.debug("Launched UserService#deleteUser(...)");
        userValidator.validateExists(id);
        userRepository.deleteById(id);
        log.debug("Ended UserService#deleteUser(...)");
    }
}
