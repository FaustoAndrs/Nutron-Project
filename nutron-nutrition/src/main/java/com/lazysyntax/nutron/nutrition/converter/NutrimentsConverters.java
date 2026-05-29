package com.lazysyntax.nutron.nutrition.converter;

import com.lazysyntax.nutron.nutrition.model.dto.NutrimentsDto;
import com.lazysyntax.nutron.nutrition.model.entity.Nutriments;


public class NutrimentsConverters {

    private NutrimentsConverters(){}

    public static NutrimentsDto toDto(Nutriments entity) {

        if (entity == null) {
            return null;
        }

        return NutrimentsDto.builder()
                .quantity(entity.getQuantity())
                .quantityUnit(entity.getQuantityUnit())
                .calories(entity.getCalories())
                .proteins(entity.getProteins())
                .carbs(entity.getCarbs())
                .fat(entity.getCarbs())
                .saturatedFat(entity.getSaturatedFat())
                .sugars(entity.getSugars())
                .salt(entity.getSalt())
                .build();
    }
    public static Nutriments toEntity(NutrimentsDto dto ) {

        if (dto == null) {
            return null;
        }

        return Nutriments.builder()
                .quantity(dto.getQuantity())
                .quantityUnit(dto.getQuantityUnit())
                .calories(dto.getCalories())
                .proteins(dto.getProteins())
                .carbs(dto.getCarbs())
                .fat(dto.getCarbs())
                .saturatedFat(dto.getSaturatedFat())
                .sugars(dto.getSugars())
                .salt(dto.getSalt())
                .build();
    }
}
