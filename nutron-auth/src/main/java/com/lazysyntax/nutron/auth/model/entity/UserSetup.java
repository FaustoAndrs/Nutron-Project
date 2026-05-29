package com.lazysyntax.nutron.auth.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSetup {

    @Id
    private String id;

    @OneToOne // Relación uno a uno con User
    @MapsId // Indica que el ID de esta entidad es también la clave foránea de la entidad User
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_settings_user")) // Nombre de la columna de la clave foránea en la tabla users_settings
    private User user; // Referencia al usuario al que pertenecen estas configuraciones

    @Column(nullable = false)
    private String weight;
    @Column(nullable = false)
    private String height;
    @Column(nullable = false)
    private String gender;
    @Column(nullable = false)
    private String age;
    @Column(nullable = false)
    private String activity;
    @Column(nullable = false)
    private String goal;
    @Column(nullable = false)
    private String formula;
    @Column(nullable = true)
    private String diet;
}
