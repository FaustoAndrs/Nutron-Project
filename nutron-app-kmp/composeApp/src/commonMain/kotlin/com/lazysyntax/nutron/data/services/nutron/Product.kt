package com.lazysyntax.nutron.data.services.nutron

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    @SerialName("product_name") val name: String? = null,
    val nutriments: Nutriments? = null
)