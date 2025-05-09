package com.example.controller;

import com.example.main.controller.UserController;
import com.example.main.dto.LoginRequest;
import com.example.main.dto.LoginResponse;
import com.example.main.entity.User;
import com.example.main.repository.UserRepository;
import com.example.main.security.JwtUtil;
import com.example.main.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    private User user;
    private LoginRequest loginRequest;

    @BeforeEach
    public void setUp() {
        // Initialize a mock user object
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");

        // Initialize a mock login request
        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");
    }

    @Test
    public void testRegisterUser() {
        // Mock the registerUser method of the userService
        when(userService.registerUser(any(User.class))).thenReturn("User registered successfully");

        // Act: Call the registerUser method in the controller
        ResponseEntity<String> response = userController.registerUser(user);

        // Assert: Check that the response is CREATED and the body matches
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());

        // Verify that the userService.registerUser method was called once
        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    public void testLogin_Success() {
        // Mock the user and passwordEncoder behavior for successful login
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtUtil.generateToken(anyString())).thenReturn("jwt-token");

        // Act: Call the login method in the controller
        ResponseEntity<?> response = userController.login(loginRequest);

        // Assert: Check that the response is OK and contains the token
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof LoginResponse);
        assertEquals("jwt-token", ((LoginResponse) response.getBody()).getToken());
    }

    @Test
    public void testLogin_Failure_InvalidCredentials() {
        // Mock the user and passwordEncoder behavior for failed login
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        // Act: Call the login method in the controller
        ResponseEntity<?> response = userController.login(loginRequest);

        // Assert: Check that the response is UNAUTHORIZED and contains an error message
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    public void testUserExists_Exists() {
        // Mock the repository to return a user when checking email
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(user));

        // Act: Call the userExists method in the controller
        ResponseEntity<Boolean> response = userController.userExists("test@example.com");

        // Assert: Check that the response is OK and indicates the user exists
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());

        // Verify that the repository's findByEmail method was called once
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    public void testUserExists_NotExists() {
        // Mock the repository to return an empty Optional when checking email
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.empty());

        // Act: Call the userExists method in the controller
        ResponseEntity<Boolean> response = userController.userExists("nonexistent@example.com");

        // Assert: Check that the response is OK and indicates the user doesn't exist
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());

        // Verify that the repository's findByEmail method was called once
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    public void testHello() {
        // Act: Call the hello method in the controller
        String response = userController.hello();

        // Assert: Check that the response matches the expected string
        assertEquals("Hello & welcome! Sumedha...... Testing working", response);
    }
}