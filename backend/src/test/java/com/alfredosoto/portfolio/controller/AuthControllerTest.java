package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.dto.LoginRequest;
import com.alfredosoto.portfolio.dto.LoginResponse;
import com.alfredosoto.portfolio.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authService);
    }

    @Test
    void shouldLoginSuccessfully() {
        LoginRequest request = new LoginRequest("admin", "secret");
        LoginResponse expectedResponse = new LoginResponse("token123");

        when(authService.login(any(LoginRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("token123", ((LoginResponse) response.getBody()).getToken());
    }

    @Test
    void shouldReturnUnauthorizedOnLoginFailure() {
        LoginRequest request = new LoginRequest("admin", "wrong");

        when(authService.login(any(LoginRequest.class))).thenThrow(new RuntimeException("Invalid credentials"));

        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }
}
