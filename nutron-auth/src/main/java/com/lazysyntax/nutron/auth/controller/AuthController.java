package com.lazysyntax.nutron.auth.controller;

import com.lazysyntax.nutron.auth.model.User;
import com.lazysyntax.nutron.auth.model.dto.*;
import com.lazysyntax.nutron.auth.service.JwtService;
import com.lazysyntax.nutron.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

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
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        User savedUser = userService.registerUser(request.toEntity());
        return ResponseEntity.ok(UserResponse.fromEntity(savedUser));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest request) {
        String refreshToken = request.refreshToken();
        
        if (jwtService.isTokenValid(refreshToken)) {
            String userIdStr = jwtService.extractUserId(refreshToken);
            Long userId = Long.parseLong(userIdStr);
            String newAccessToken = jwtService.generateAccessToken(userId);
            String newRefreshToken = jwtService.generateRefreshToken(userId);

            return ResponseEntity.ok(new AuthResponse(newAccessToken, newRefreshToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token de refresco inválido o expirado");
        }
    }
}
