package com.lazysyntax.nutron.domain.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meal(
    val id: String? = null,
    @SerialName("meal_name") val name: String,
    val foods: List<Food>? = null,
    val recipes: List<Recipe>? = null,
    val date: LocalDate? = null
)