package com.lazysyntax.nutron.nutrition.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutrimentsDto {
    private String quantity = "100";
    private String quantityUnit = "g";
    private Double calories;
    private Double proteins;
    private Double carbs;
    private Double fat;
    private Double saturatedFat;
    private Double sugars;
    private Double salt;

}
