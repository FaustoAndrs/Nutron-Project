package com.lazysyntax.nutron.data.remote.food

import com.lazysyntax.nutron.data.local.food.FoodEntity
import com.lazysyntax.nutron.data.remote.meal.toDto
import com.lazysyntax.nutron.data.remote.meal.toEntity
import kotlin.time.Clock

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

fun FoodDto.toEntity(): FoodEntity{
    return FoodEntity(
        foodId = foodId!!,
        barcode = barcode,
        userId = userId,
        name = name,
        lastUpdate = Clock.System.now().toEpochMilliseconds(),
        nameEs = nameEs,
        nameEn = nameEn,
        isSynced = false,
        nutriments = nutriments?.toEntity(),
        nutriscoreGrade = nutriscoreGrade,
        brands = brands
    )
}