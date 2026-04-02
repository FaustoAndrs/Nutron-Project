package com.lazysyntax.nutron.data.services.nutron

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meal(
    @SerialName("meal_name") val name: String? = null,
    val products: List<Product>? = null
)


