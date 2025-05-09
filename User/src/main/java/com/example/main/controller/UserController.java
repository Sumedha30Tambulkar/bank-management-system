package com.example.main.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.main.dto.LoginRequest;
import com.example.main.dto.LoginResponse;
import com.example.main.entity.User;
import com.example.main.repository.UserRepository;
import com.example.main.security.JwtUtil;
import com.example.main.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    /**
     * Endpoint to register a new user.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        logger.info("Registering user with email: {}", user.getEmail());
        String result = userService.registerUser(user);
        logger.info("Registration result: {}", result);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * Endpoint to authenticate and login a user.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Login attempt for email: {}", loginRequest.getEmail());

        User user = userService.getUserByEmail(loginRequest.getEmail());

        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getEmail());
            logger.info("Login successful. Token generated for user: {}", user.getEmail());
            return ResponseEntity.ok(new LoginResponse(token));
        } else {
            logger.warn("Login failed for email: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    /**
     * Check if a user exists by email.
     */
    @GetMapping("/api/user/exists/{email}")
    public ResponseEntity<Boolean> userExists(@PathVariable String email) {
        logger.debug("Checking if user exists for email: {}", email);
        boolean exists = userRepository.findByEmail(email).isPresent();
        logger.info("User exists for {}: {}", email, exists);
        return ResponseEntity.ok(exists);
    }

    /**
     * Test endpoint to verify API is up.
     */
    @GetMapping("/dashboard")
    public String hello() {
        logger.info("Accessed dashboard test endpoint");
        return "Hello & welcome! Sumedha...... Testing working";
    }
}
