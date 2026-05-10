package com.lazysyntax.nutron.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meal(
    @SerialName("meal_name") val name: String? = null,
    val foods: List<Food>? = null
)