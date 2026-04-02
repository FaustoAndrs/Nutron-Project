package com.lazysyntax.nutron.main.ui.features.diary

import com.lazysyntax.nutron.data.services.nutron.Meal
import com.lazysyntax.nutron.data.services.nutron.Product

data class DiaryUiState(
    val barcode: String,
    val productName: String,
    val product: Product?,
    val productList: List<Product>?,
    val meals: List<Meal>?
) {
    constructor() : this(
        barcode = "3017624010701",
        productName = "Nutella",
        product = null,
        productList = emptyList(),
        meals = listOf(
            Meal(name = "Desayuno"),
            Meal(name = "Almuerzo"),
            Meal(name = "Cena")
        )
    )
}
