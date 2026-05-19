package com.lazysyntax.nutron.data.remote.food

import com.lazysyntax.nutron.data.remote.meal.NutrimentsDto
import kotlinx.serialization.Serializable

@Serializable
data class FoodDto(
    val foodId: String? = null,
    val barcode: String?,
    val userId: String?,
    val name: String?,
    val nameEs: String?,
    val nameEn: String?,
    val nutriments: NutrimentsDto?,
    val nutriscoreGrade: String?,
    val brands: String?
)
