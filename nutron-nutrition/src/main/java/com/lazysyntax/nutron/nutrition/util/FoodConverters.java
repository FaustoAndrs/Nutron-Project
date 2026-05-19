package com.lazysyntax.nutron.nutrition.util;


import com.lazysyntax.nutron.nutrition.model.dto.MealRequest.FoodDto;
import com.lazysyntax.nutron.nutrition.model.entity.Food;
import com.lazysyntax.nutron.nutrition.model.entity.Nutriments;

import static com.lazysyntax.nutron.nutrition.util.NutrimentsConverters.convertToDto;
import static com.lazysyntax.nutron.nutrition.util.NutrimentsConverters.convertToEntity;

public final class FoodConverters {

    // Private constructor to prevent instantiation
    private FoodConverters() {
        // Utility class
    }

    public static FoodDto toDto(Food food) {
        if (food == null) {
            return null;
        }
        FoodDto dto = new FoodDto();
        dto.setFoodId(food.getFoodId());
        dto.setBarcode(food.getBarcode());
        dto.setUserId(food.getUserId());
        dto.setName(food.getName());
        dto.setNameEs(food.getNameEs());
        dto.setNameEn(food.getNameEn());
        dto.setNutriments(convertToDto(food.getNutriments()));
        dto.setNutriscoreGrade(food.getNutriscoreGrade());
        dto.setBrands(food.getBrands());
        return dto;
    }

    /**
     * Converts a FoodDto to a new Food entity.
     * This method is intended for creating new Food entities, so foodId is not set.
     *
     * @param foodDto The DTO to convert.
     * @return A new Food entity.
     */
    public static Food toEntity(FoodDto foodDto) {
        if (foodDto == null) {
            return null;
        }
        Food food = new Food();
        food.setFoodId(foodDto.getFoodId());
        food.setBarcode(foodDto.getBarcode());
        food.setUserId(foodDto.getUserId());
        food.setName(foodDto.getName());
        food.setNameEs(foodDto.getNameEs());
        food.setNameEn(foodDto.getNameEn());
        food.setNutriments(convertToEntity(foodDto.getNutriments()));
        food.setNutriscoreGrade(foodDto.getNutriscoreGrade());
        food.setBrands(foodDto.getBrands());
        return food;
    }

    /**
     * Updates an existing Food entity with data from a FoodDto.
     *
     * @param foodDto The DTO containing updated data.
     * @param existingFood The existing Food entity to update.
     */
    public static void updateEntity(FoodDto foodDto, Food existingFood) {
        if (foodDto == null || existingFood == null) {
            return;
        }
        // foodId is not updated here, it's used for identification
        existingFood.setBarcode(foodDto.getBarcode());
        existingFood.setUserId(foodDto.getUserId());
        existingFood.setName(foodDto.getName());
        existingFood.setNameEs(foodDto.getNameEs());
        existingFood.setNameEn(foodDto.getNameEn());
        existingFood.setNutriments(convertToEntity(foodDto.getNutriments())); // Assuming NutrimentsConverters.convertToEntity handles updates or creates new
        existingFood.setNutriscoreGrade(foodDto.getNutriscoreGrade());
        existingFood.setBrands(foodDto.getBrands());
    }
}