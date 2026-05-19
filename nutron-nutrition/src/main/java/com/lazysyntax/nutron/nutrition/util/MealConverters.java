package com.lazysyntax.nutron.nutrition.util;

import com.lazysyntax.nutron.nutrition.model.dto.MealRequest.MealDto;
import com.lazysyntax.nutron.nutrition.model.dto.MealRequest.MealFoodSnapshotDto;
import com.lazysyntax.nutron.nutrition.model.entity.Meal;
import com.lazysyntax.nutron.nutrition.model.entity.MealFoodSnapshot;
import com.lazysyntax.nutron.nutrition.model.entity.Nutriments;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MealConverters {

    private MealConverters(){}

    public static MealDto convertToDto(Meal meal) {
        MealDto dto = new MealDto();
        dto.setId(meal.getId());
        dto.setUserId(meal.getUserId());
        dto.setName(meal.getName());
        dto.setDate(meal.getDate().toString());
        dto.setFoods(meal.getFoods().stream()
                .map(MealConverters::convertToDto)
                .collect(Collectors.toList()));
        return dto;
    }

    public static Meal convertToEntity(MealDto dto ) {
        Meal entity = new Meal();
        entity.setId(dto.getId());
        entity.setUserId(dto.getUserId());
        entity.setName(dto.getName());
        entity.setDate(LocalDate.parse(dto.getDate()));
        entity.setFoods(dto.getFoods().stream()
                .map(MealConverters::convertToEntity)
                .collect(Collectors.toList()));
        return entity;
    }

    public static void updateEntity(MealDto mealDto, Meal existingMeal) {
        if (mealDto == null || existingMeal == null) {
            return;
        }
        existingMeal.setUserId(mealDto.getUserId());
        existingMeal.setName(mealDto.getName());
        existingMeal.setDate(LocalDate.parse(mealDto.getDate()));

        if (existingMeal.getFoods() != null) {
            existingMeal.getFoods().clear();
        } else {
            existingMeal.setFoods(new ArrayList<>());
        }
        if (mealDto.getFoods() != null) {
            existingMeal.getFoods().addAll(mealDto.getFoods().stream()
                    .map(MealConverters::convertToEntity)
                    .collect(Collectors.toList()));
        }
    }

    public static MealFoodSnapshotDto convertToDto(MealFoodSnapshot entity) {
        MealFoodSnapshotDto dto = new MealFoodSnapshotDto();
        dto.setSnapshotId(entity.getSnapshotId());
        dto.setFoodId(entity.getFoodId());
        dto.setName(entity.getName());
        dto.setBarcode(entity.getBarcode());
        dto.setNutriments(
                NutrimentsConverters.convertToDto(entity.getNutriments())
        );

        return dto;
    }

    public static MealFoodSnapshot convertToEntity(MealFoodSnapshotDto dto) {
        MealFoodSnapshot entity = new MealFoodSnapshot();

        Nutriments nutriments = NutrimentsConverters.convertToEntity(dto.getNutriments());

        // entity.setSnapshotId(dto.getSnapshotId()); // <--- Eliminamos esta línea
        entity.setFoodId(dto.getFoodId());
        entity.setName(dto.getName());
        entity.setBarcode(dto.getBarcode());
        entity.setNutriments(nutriments);

        return entity;
    }
}