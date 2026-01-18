package com.alfredosoto.portfolio.service;

import com.alfredosoto.portfolio.dto.LoginRequest;
import com.alfredosoto.portfolio.dto.LoginResponse;
import com.alfredosoto.portfolio.entity.UserEntity;
import com.alfredosoto.portfolio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Value("${admin.password}")
    private String defaultAdminPassword;

    @Value("${admin.username}")
    private String defaultAdminUsername;

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }


    public LoginResponse login(LoginRequest request) {
        // First check DB
        Optional<UserEntity> userOpt = userRepository.findByUsername(request.getUsername());
        
        if (userOpt.isPresent()) {
            if (verifyPassword(request.getPassword(), userOpt.get().getPasswordHash())) {
                String token = jwtService.generateToken(request.getUsername());
                return new LoginResponse(token);
            }
        } else {
            // Fallback for initial setup if no users exist (optional, or rely on DataSeeder)
            // But DataSeeder should have run.
            // However, to keep backward compatibility with "admin" user if seeded:
            if ("admin".equals(request.getUsername()) && defaultAdminPassword.equals(request.getPassword())) {
                 // Migrate or just allow? Better to ensure DataSeeder creates it.
                 // For now, let's assume DataSeeder works.
            }
        }
        
        throw new IllegalStateException("Invalid credentials");
    }

    public void register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("User already exists");
        }
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPasswordHash(hashPassword(password));
        user.setRole("ADMIN");
        userRepository.save(user);
    }

    public void createAdminUserIfNotFound() {
        // Sync admin user from properties/env vars
        // This ensures that if the environment variables change, the DB is updated on restart
        UserEntity user = userRepository.findByUsername(defaultAdminUsername)
                .orElseGet(() -> {
                    UserEntity newUser = new UserEntity();
                    newUser.setUsername(defaultAdminUsername);
                    newUser.setRole("ADMIN");
                    return newUser;
                });
        
        // Always update the password hash to match the current configuration
        user.setPasswordHash(hashPassword(defaultAdminPassword));
        userRepository.save(user);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Error hashing password", e);
        }
    }

    private boolean verifyPassword(String rawPassword, String hashedPassword) {
        return hashPassword(rawPassword).equals(hashedPassword);
    }
}
