package com.lazysyntax.nutron.data.services.nutron

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    val product: Product? = null,
    val status: Int? = null
)

@Serializable
data class SearchResponse(
    val products: List<Product>? = null,
    val count: Int? = null
)


@Serializable
data class Nutriments(
    @SerialName("energy-kcal_100g") val calories: Double? = null,
    @SerialName("proteins_100g") val proteins: Double? = null,
    @SerialName("carbohydrates_100g") val carbs: Double? = null,
    @SerialName("fat_100g") val fat: Double? = null,
    @SerialName("sugars_100g") val sugars: Double? = null,
    @SerialName("salt_100g") val salt: Double? = null
)
