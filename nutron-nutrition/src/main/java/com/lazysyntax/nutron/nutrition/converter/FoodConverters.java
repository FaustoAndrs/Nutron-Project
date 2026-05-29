package com.lazysyntax.nutron.nutrition.converter;


import com.lazysyntax.nutron.nutrition.model.dto.FoodDto;
import com.lazysyntax.nutron.nutrition.model.entity.Food;

public final class FoodConverters {

    private FoodConverters() {
    }

    public static FoodDto toDto(Food food) {

        if (food == null) {
            return null;
        }

        return FoodDto.builder()
                .foodId(food.getFoodId())
                .barcode(food.getBarcode())
                .userId(food.getUserId())
                .name(food.getName())
                .nameEs(food.getNameEs())
                .nameEn(food.getNameEn())
                .nutriments(NutrimentsConverters.toDto(food.getNutriments()))
                .nutriscoreGrade(food.getNutriscoreGrade())
                .brands(food.getBrands())
                .build();
    }

    public static Food toEntity(FoodDto foodDto) {

        if (foodDto == null) {
            return null;
        }

        return Food.builder()
                .foodId(foodDto.getFoodId())
                .barcode(foodDto.getBarcode())
                .userId(foodDto.getUserId())
                .name(foodDto.getName())
                .nameEs(foodDto.getNameEs())
                .nameEn(foodDto.getNameEn())
                .nutriments(NutrimentsConverters.toEntity(foodDto.getNutriments()))
                .nutriscoreGrade(foodDto.getNutriscoreGrade())
                .brands(foodDto.getBrands())
                .build();
    }
}