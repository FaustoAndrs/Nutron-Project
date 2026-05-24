package com.lazysyntax.nutron.data.remote.meal

import kotlinx.serialization.Serializable

@Serializable
data class MealDto(
    val id: String,
    val userId: String? = null,
    val name: String,
    val date: String, // LocalDate as String "YYYY-MM-DD"
    val foods: List<MealFoodSnapshotDto> = emptyList()
)

@Serializable
data class MealFoodSnapshotDto(
    val snapshotId: String,
    val foodId: String,
    val name: String?,
    val barcode: String?,
    val nutriments: NutrimentsDto?
)

@Serializable
data class NutrimentsDto(
    val quantity: String? = "100",
    val quantityUnit: String? = "g",
    val calories: Double?,
    val proteins: Double?,
    val carbs: Double?,
    val fat: Double?,
    val saturatedFat: Double?,
    val sugars: Double?,
    val salt: Double?
)
