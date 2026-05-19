package com.lazysyntax.nutron.data.remote.food

import com.lazysyntax.nutron.data.local.food.FoodEntity
import com.lazysyntax.nutron.data.remote.meal.toDto

fun FoodEntity.toDto(): FoodDto {
    return FoodDto(
        foodId = foodId,
        barcode = barcode,
        userId = userId,
        name = name,
        nameEs = nameEs,
        nameEn = nameEn,
        nutriments = nutriments?.toDto(),
        nutriscoreGrade = nutriscoreGrade,
        brands = brands
    )
}
