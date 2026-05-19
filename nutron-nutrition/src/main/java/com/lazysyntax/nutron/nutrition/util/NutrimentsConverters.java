package com.lazysyntax.nutron.nutrition.util;

import com.lazysyntax.nutron.nutrition.model.dto.MealRequest.NutrimentsDto;
import com.lazysyntax.nutron.nutrition.model.entity.Nutriments;


public class NutrimentsConverters {
    // --- Helper methods for Entity to DTO conversion (for response) ---
    private NutrimentsConverters(){}

    public static NutrimentsDto convertToDto(Nutriments entity) {
        if (entity == null) {
            return null;
        }
        NutrimentsDto dto = new NutrimentsDto();
        dto.setQuantity(entity.getQuantity());
        dto.setQuantityUnit(entity.getQuantityUnit());
        dto.setCalories(entity.getCalories());
        dto.setProteins(entity.getProteins());
        dto.setCarbs(entity.getCarbs());
        dto.setFat(entity.getFat());
        dto.setSaturatedFat(entity.getSaturatedFat());
        dto.setSugars(entity.getSugars());
        dto.setSalt(entity.getSalt());
        return dto;
    }
    public static Nutriments convertToEntity(NutrimentsDto dto ) {
        if (dto == null) {
            return null;
        }
        Nutriments entity = new Nutriments();
        entity.setQuantity(dto.getQuantity());
        entity.setQuantityUnit(dto.getQuantityUnit());
        entity.setCalories(dto.getCalories());
        entity.setProteins(dto.getProteins());
        entity.setCarbs(dto.getCarbs());
        entity.setFat(dto.getFat());
        entity.setSaturatedFat(dto.getSaturatedFat());
        entity.setSugars(dto.getSugars());
        entity.setSalt(dto.getSalt());
        return entity;
    }
}
