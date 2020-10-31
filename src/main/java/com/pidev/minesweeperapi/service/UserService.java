package com.pidev.minesweeperapi.service;

import com.pidev.minesweeperapi.model.User;
import com.pidev.minesweeperapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * User Service
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all the users.
     * @return a list of {@link User}s.
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Retrieve a user with a given name.
     * @param name the user name.
     * @return a {@link User}.
     */
    public Optional<User> findByName(final String name) {
        return userRepository.findByName(name);
    }

    /**
     * Creates a new user.
     * @param user the user to be stored.
     * @return a {@link User}.
     */
    public User create(final User user) {
        return userRepository.save(user);
    }

}
