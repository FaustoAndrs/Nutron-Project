package com.lazysyntax.nutron.nutrition.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "foods")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food {
    @Id
    private String foodId;
    private String barcode;
    private String userId;
    private String name;
    private String nameEs;
    private String nameEn;

    @Embedded
    private Nutriments nutriments;

    private String nutriscoreGrade;
    private String brands;
}
