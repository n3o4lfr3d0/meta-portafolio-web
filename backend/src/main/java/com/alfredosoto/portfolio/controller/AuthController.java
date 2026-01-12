package com.alfredosoto.portfolio.controller;

import com.alfredosoto.portfolio.dto.LoginRequest;
import com.alfredosoto.portfolio.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${admin.password:admin123}")
    private String adminPassword;

    @Value("${admin.token:secret-admin-token-2026}")
    private String adminToken;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (adminPassword.equals(request.getPassword())) {
            return ResponseEntity.ok(new LoginResponse(adminToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
