package com.lazysyntax.nutron.auth.service;

import com.lazysyntax.nutron.auth.model.entity.User;
import com.lazysyntax.nutron.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service //Marca esta clase como un componente de servicio en Spring
public class UserService {
    @Autowired // Inyecta una instancia de UserRepository
    private UserRepository userRepository;

    @Autowired // Inyecta una instancia de BCryptPasswordEncoder para encriptar contraseñas
    private BCryptPasswordEncoder passwordEncoder;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //Verifica las credenciales del usuario. Busca un usuario en la BD y compara sus contraseñas.
    public boolean verifyCredentials(String email, String rawPassword) {
        return userRepository
                .findByEmail(email)
                .map(user -> passwordEncoder
                        .matches(rawPassword, user.getPassword()))
                .orElse(false);
    }

    // Registra un nuevo usuario en la base de datos.
    public User registerUser(User user) {
        // Encripta la contraseña antes de guardarla en MySQL
        String encodedPassword = passwordEncoder
                .encode(user.getPassword());

        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }
}
