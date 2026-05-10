package com.lazysyntax.nutron.data.services.openFoodFactsApi

import com.lazysyntax.nutron.models.Food
import kotlinx.serialization.Serializable

@Serializable
data class FetchResponse(
    val food: Food? = null,
    val status: Int? = null
)

@Serializable
data class SearchResponse(
    val foods: List<Food>? = null,
    val count: Int? = null
)
