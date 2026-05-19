package com.lazysyntax.nutron.data.local.food

import com.lazysyntax.nutron.domain.models.Food
import com.lazysyntax.nutron.domain.models.Nutriments
import kotlin.math.round
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


@OptIn(ExperimentalUuidApi::class)
fun Food.toEntity(
    userId: String?,
    isSynced: Boolean = false,
    lastUpdate: Long = Clock.System.now().toEpochMilliseconds()
): FoodEntity {
    return FoodEntity(
        foodId = id ?: Uuid.random().toString(),
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
        calories = calories?.round(2),
        proteins = proteins?.round(2),
        carbs = carbs?.round(2),
        fat = fat?.round(2),
        saturatedFat = saturatedFat?.round(2),
        sugars = sugars?.round(2),
        salt = salt?.round(2),
        quantity = quantity ?: "100",
        quantityUnit = quantityUnit ?: "g"
    )
}

private fun Double?.round(decimals: Int): Double? {
    if (this == null) return null
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

fun FoodEntity.toDomain(): Food {
    return Food(
        id = foodId,
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
        calories = calories?.round(2),
        proteins = proteins?.round(2),
        carbs = carbs?.round(2),
        fat = fat?.round(2),
        saturatedFat = saturatedFat?.round(2),
        sugars = sugars?.round(2),
        salt = salt?.round(2),
        quantity = quantity ?: "100",
        quantityUnit = quantityUnit ?: "g"
    )
}
