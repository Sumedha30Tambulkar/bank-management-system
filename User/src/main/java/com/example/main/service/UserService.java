package com.example.main.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.main.entity.Gender;
import com.example.main.entity.User;
import com.example.main.repository.UserRepository;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Get a user by email. Returns null if not found.
     */
    public User getUserByEmail(String email) {
        logger.debug("Fetching user by email: {}", email);
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * Registers a new user if the email doesn't already exist.
     */
    public String registerUser(User user) {
        logger.info("Attempting to register user with email: {}", user.getEmail());

        if (userRepository.existsByEmail(user.getEmail())) {
            logger.warn("Registration failed — user already exists: {}", user.getEmail());
            return "User already exists";
        }

        // Validate gender enum
        try {
            Gender gender = Gender.valueOf(user.getGender().name());
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.error("Invalid gender value provided: {}", user.getGender());
            return "Invalid gender value! Allowed values are: MALE, FEMALE, OTHER";
        }

        // Map fields and encode password
        User newUser = new User();
        newUser.setUserName(user.getUserName());
        newUser.setDob(user.getDob());
        newUser.setGender(user.getGender());
        newUser.setAddress(user.getAddress());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword())); // Encrypt password
        newUser.setRole(user.getRole());

        userRepository.save(newUser);
        logger.info("User registered successfully: {}", user.getEmail());

        return "User registered successfully!";
    }

    /**
     * Verifies user login credentials.
     */
    public String loginUser(String email, String rawPassword) {
        logger.info("Login attempt for user: {}", email);

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                logger.info("Login successful for user: {}", email);
                return "Login successful";
            } else {
                logger.warn("Login failed due to invalid password for user: {}", email);
                return "Invalid password";
            }
        } else {
            logger.warn("Login failed — user not found: {}", email);
            return "User not found";
        }
    }
}
