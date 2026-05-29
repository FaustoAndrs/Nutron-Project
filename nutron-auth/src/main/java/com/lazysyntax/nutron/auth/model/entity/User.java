package com.lazysyntax.nutron.auth.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity //Entidad JPA que mapea una table en la base de datos
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_users_username", columnNames = "user_name")
})
@Data // Genera getters, setters, toString, equals y hashCode automáticamente
@NoArgsConstructor // Genera un constructor sin argumentos
@AllArgsConstructor // Genera un constructor con todos los argumentos
@Builder //Perpmite crear instancias usando el patron builder
public class User {

    @Id // Marca este campo como la clave primaria de la entidad.
    private String id; // El ID del usuario, almacena el UUIDs generado por la aplicacion de frontend.

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Esto permite recibir la clave al registrarse, pero NO enviarla el response
    private String password;


    /* Relación uno a uno con UserSetup,
    * 'mappedBy' indica que la relación es gestionada por el campo 'user' en la entidad UserSetup
    * 'cascade = CascadeType.ALL' significa que las operaciones en User (entidad padre) tambien se propagan a UserSetup (entidad hija)
    * 'orphanRemoval = true' elimina UserSetup si se elimina User
    * * */
   @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserSetup userSetup;

}