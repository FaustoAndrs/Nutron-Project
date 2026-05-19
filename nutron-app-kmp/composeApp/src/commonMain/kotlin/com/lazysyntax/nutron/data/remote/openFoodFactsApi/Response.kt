package com.lazysyntax.nutron.data.remote.openFoodFactsApi

import com.lazysyntax.nutron.domain.models.Food
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FetchResponse(
    @SerialName("product")
    val food: Food? = null,
    val status: Int? = null
)

@Serializable
data class SearchResponse(
    @SerialName("products")
    val foods: List<Food>? = null,
    val count: Int? = null
)
