package com.lazysyntax.nutron.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    @SerialName("recipe_name") val name: String,
    val ingredients: List<Food>,
    )