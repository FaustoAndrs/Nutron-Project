package com.lazysyntax.nutron.auth.repository;

import com.lazysyntax.nutron.auth.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
/* La clase UserRepository extiende a JpaRepository para proporcionarle métodos CRUD (inert,delete,find...)
* */
public interface UserRepository extends JpaRepository<User, String> { //
    Optional<User> findByEmail(String email); //Implementacion automatica generada por Spring Data JPA
}




