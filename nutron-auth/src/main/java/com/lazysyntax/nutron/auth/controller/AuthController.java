package com.lazysyntax.nutron.auth.controller;

import com.lazysyntax.nutron.auth.model.entity.User;
import com.lazysyntax.nutron.auth.model.dto.*;
import com.lazysyntax.nutron.auth.service.JwtService;
import com.lazysyntax.nutron.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.TimeZone;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOptional = userService.findByEmail(request.email())
                .filter(user -> userService.verifyCredentials(request.email(), request.password()));

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String accessToken = jwtService.generateAccessToken(user.getId());
            String refreshToken = jwtService.generateRefreshToken(user.getId());

            return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request, TimeZone timeZone) {

        try {

            User savedUser = userService.registerUser(request.toEntity());
            return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.toResponse(savedUser));

        } catch (DataIntegrityViolationException e) {
            String errorMessage = e.getMessage();

            if (errorMessage != null && (errorMessage.contains("uk_users_email") || errorMessage.contains("uk_users_username"))) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResponse(HttpStatus.CONFLICT.value(), "El email o nombre de usuario ya está registrado.", LocalDateTime.now()));
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error de integridad de datos inesperado.", LocalDateTime.now()));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ocurrió un error inesperado durante el registro.", LocalDateTime.now()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest request) {
        String refreshToken = request.refreshToken();

        if (jwtService.isTokenValid(refreshToken)) {
            String userIdStr = jwtService.extractUserId(refreshToken);
            String newAccessToken = jwtService.generateAccessToken(userIdStr);
            String newRefreshToken = jwtService.generateRefreshToken(userIdStr);

            return ResponseEntity.ok(new AuthResponse(newAccessToken, newRefreshToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token de refresco inválido o expirado");
        }
    }
}
