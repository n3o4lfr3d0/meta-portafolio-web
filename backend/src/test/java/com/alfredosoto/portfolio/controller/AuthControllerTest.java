package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.dto.LoginRequest;
import com.alfredosoto.portfolio.dto.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController();
        ReflectionTestUtils.setField(authController, "adminPassword", "admin123");
        ReflectionTestUtils.setField(authController, "adminToken", "secret-token");
    }

    @Test
    void shouldReturnTokenWhenPasswordIsCorrect() {
        LoginRequest request = new LoginRequest();
        request.setPassword("admin123");

        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof LoginResponse);
        assertEquals("secret-token", ((LoginResponse) response.getBody()).getToken());
    }

    @Test
    void shouldReturnUnauthorizedWhenPasswordIsIncorrect() {
        LoginRequest request = new LoginRequest();
        request.setPassword("wrong-password");

        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }
}
