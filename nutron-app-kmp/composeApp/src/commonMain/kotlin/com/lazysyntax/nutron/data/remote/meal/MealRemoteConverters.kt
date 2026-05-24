package com.lazysyntax.nutron.data.remote.meal

import com.lazysyntax.nutron.data.local.food.NutrimentsEntity
import com.lazysyntax.nutron.data.local.meal.MealEntity
import com.lazysyntax.nutron.data.local.meal.MealFoodSnapshotEntity
import com.lazysyntax.nutron.data.local.meal.MealWithFoods
import kotlinx.datetime.LocalDate

fun MealWithFoods.toDto(): MealDto {
    return MealDto(
        id = meal.id,
        userId = meal.userId,
        name = meal.name,
        date = meal.date.toString(),
        foods = snapshots.map { it.toDto() }
    )
}
fun MealDto.toEntity(isSynced: Boolean = false) : MealEntity {
    return MealEntity(
        id = id,
        userId = userId,
        name = name,
        date = LocalDate.parse(date),
        isSynced = isSynced
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
fun MealFoodSnapshotDto.toEntity(mealId: String): MealFoodSnapshotEntity{
    return MealFoodSnapshotEntity(
        snapshotId = snapshotId,
        mealId =mealId,
        foodId = foodId,
        name = name,
        barcode = barcode,
        nutriments = nutriments?.toEntity()
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

fun NutrimentsDto.toEntity(): NutrimentsEntity{
    return NutrimentsEntity(
        quantity = quantity,
        quantityUnit = quantityUnit,
        calories = calories,
        proteins = proteins,
        carbs = carbs,
        fat = fat,
        saturatedFat = saturatedFat,
        sugars = sugars,
        salt = salt
    )

}