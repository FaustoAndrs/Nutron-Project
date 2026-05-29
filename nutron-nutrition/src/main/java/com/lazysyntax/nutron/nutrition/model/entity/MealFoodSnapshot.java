package com.lazysyntax.nutron.nutrition.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "meal_food_snapshots")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealFoodSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String snapshotId;
    private String foodId;// Referencia al Id de la comida/producto con los valores iniciales.
    private String name;
    private String barcode;

    @Embedded
    private Nutriments nutriments;
}
