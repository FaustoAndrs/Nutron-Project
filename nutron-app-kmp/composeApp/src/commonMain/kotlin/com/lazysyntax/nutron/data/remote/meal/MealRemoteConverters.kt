package com.lazysyntax.nutron.data.remote.meal

import com.lazysyntax.nutron.data.local.food.NutrimentsEntity
import com.lazysyntax.nutron.data.local.meal.MealFoodSnapshotEntity
import com.lazysyntax.nutron.data.local.meal.MealWithFoods

fun MealWithFoods.toDto(): MealDto {
    return MealDto(
        id = meal.id,
        userId = meal.userId,
        name = meal.name,
        date = meal.date.toString(),
        foods = snapshots.map { it.toDto() }
    )
}

fun MealFoodSnapshotEntity.toDto(): MealFoodSnapshotDto {
    return MealFoodSnapshotDto(
        snapshotId = snapshotId,
        foodId = foodId,
        name = name,
        barcode = barcode,
        nutriments = nutriments?.toDto()
    )
}

fun NutrimentsEntity.toDto(): NutrimentsDto {
    return NutrimentsDto(
        quantity = quantity ?: "100",
        quantityUnit = quantityUnit ?: "g",
        calories = calories,
        proteins = proteins,
        carbs = carbs,
        fat = fat,
        saturatedFat = saturatedFat,
        sugars = sugars,
        salt = salt
    )
}
