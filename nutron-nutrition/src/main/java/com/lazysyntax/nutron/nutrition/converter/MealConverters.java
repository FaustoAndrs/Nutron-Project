package com.lazysyntax.nutron.nutrition.converter;

import com.lazysyntax.nutron.nutrition.model.dto.MealDto;
import com.lazysyntax.nutron.nutrition.model.dto.MealFoodSnapshotDto;
import com.lazysyntax.nutron.nutrition.model.dto.NutrimentsDto;
import com.lazysyntax.nutron.nutrition.model.entity.Meal;
import com.lazysyntax.nutron.nutrition.model.entity.MealFoodSnapshot;
import com.lazysyntax.nutron.nutrition.model.entity.Nutriments;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class MealConverters {

    private MealConverters() {
    }

    public static MealDto toDto(Meal meal) {
        return MealDto.builder()
                .id(meal.getId())
                .userId(meal.getUserId())
                .name(meal.getName())
                .date(meal.getDate().toString())
                .foods(meal.getFoods().stream()
                        .map(MealConverters::toDto)
                        .collect(Collectors.toList())).build();
    }

    public static Meal toEntity(MealDto dto) {

        return Meal.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .name(dto.getName())
                .date(LocalDate.parse(dto.getDate()))
                .foods(dto.getFoods().stream()
                        .map(MealConverters::toEntity)
                        .collect(Collectors.toList())).build();

    }

    public static MealFoodSnapshotDto toDto(MealFoodSnapshot entity) {

        NutrimentsDto nutriments = NutrimentsConverters.toDto(entity.getNutriments());

        return MealFoodSnapshotDto.builder()
                .snapshotId(entity.getSnapshotId())
                .foodId(entity.getFoodId())
                .name(entity.getName())
                .barcode(entity.getBarcode())
                .nutriments(nutriments)
                .build();
    }

    public static MealFoodSnapshot toEntity(MealFoodSnapshotDto dto) {

        Nutriments nutriments = NutrimentsConverters.toEntity(dto.getNutriments());

        return MealFoodSnapshot.builder().foodId(dto.getFoodId())
                .name(dto.getName())
                .barcode(dto.getBarcode())
                .nutriments(nutriments)
                .build();
    }
}