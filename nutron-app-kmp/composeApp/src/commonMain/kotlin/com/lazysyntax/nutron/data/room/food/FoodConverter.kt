package com.lazysyntax.nutron.data.room.food

import com.lazysyntax.nutron.models.Food
import com.lazysyntax.nutron.models.Nutriments
import kotlin.time.Clock


fun Food.toEntity(
    userId: String?,
    isSynced: Boolean = false,
    lastUpdate: Long = Clock.System.now().toEpochMilliseconds()
): FoodEntity {
    return FoodEntity(
        barcode = barcode?: "",
        name = name,
        nameEs = nameEs,
        nameEn = nameEn,
        nutriscoreGrade = nutriscoreGrade,
        brands = brands,
        nutriments = nutriments?.toEntity(),
        userId = userId,
        isSynced = isSynced,
        lastUpdate = lastUpdate,
    )
}

fun Nutriments.toEntity(): NutrimentsEntity {
    return NutrimentsEntity(
        calories = calories,
        proteins = proteins,
        carbs = carbs,
        fat = fat,
        saturatedFat = saturatedFat,
        sugars = sugars,
        salt = salt,
        quantity = quantity,
        quantityUnit = quantityUnit
    )
}

fun FoodEntity.toDomain(): Food {
    return Food(
        barcode = barcode,
        name = name,
        nameEs = nameEs,
        nameEn = nameEn,
        nutriscoreGrade = nutriscoreGrade,
        brands = brands,
        nutriments = nutriments?.toDomain()
    )
}

fun NutrimentsEntity.toDomain(): Nutriments {
    return Nutriments(
        calories = calories,
        proteins = proteins,
        carbs = carbs,
        fat = fat,
        saturatedFat = saturatedFat,
        sugars = sugars,
        salt = salt
    )
}
