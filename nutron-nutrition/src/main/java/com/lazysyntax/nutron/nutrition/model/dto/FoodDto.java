package com.lazysyntax.nutron.nutrition.model.dto;

import lombok.*;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodDto {
    private String foodId;
    private String barcode;
    private String userId;
    private String name;
    private String nameEs;
    private String nameEn;
    private NutrimentsDto nutriments;
    private String nutriscoreGrade;
    private String brands;
}
