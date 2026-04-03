package com.lazysyntax.nutron.auth.controller;


import com.lazysyntax.nutron.auth.model.LoginRequest;
import com.lazysyntax.nutron.auth.model.RegisterRequest;
import com.lazysyntax.nutron.auth.model.User;
import com.lazysyntax.nutron.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        boolean isValid = userService.verifyCredentials(request.getEmail(), request.getPassword());

        if (isValid) {
            // Aquí podrías retornar un objeto con el ID del usuario
            return ResponseEntity.ok("Login exitoso");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // Mapeamos el DTO a la Entidad
            User newUser = new User();
            newUser.setUsername(request.getUsername());
            newUser.setFullname(request.getFullname());
            newUser.setEmail(request.getEmail());
            newUser.setPassword(request.getPassword()); // El servicio se encarga de encriptar


            User savedUser = userService.registerUser(newUser);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al registrar: " + e.getMessage());
        }

    }

}