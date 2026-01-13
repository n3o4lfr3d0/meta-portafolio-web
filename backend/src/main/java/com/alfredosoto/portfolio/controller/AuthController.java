package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.dto.LoginRequest;
import com.alfredosoto.portfolio.dto.LoginResponse;
import com.alfredosoto.portfolio.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Default to "admin" if username is missing (backward compatibility/simple UI)
            if (request.getUsername() == null || request.getUsername().isEmpty()) {
                request.setUsername("admin");
            }
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
