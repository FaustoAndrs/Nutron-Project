package com.lazysyntax.nutron.auth.repository;



import com.lazysyntax.nutron.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Spring genera la consulta automáticamente basándose en el nombre del método
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
