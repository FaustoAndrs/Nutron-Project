package com.lazysyntax.nutron.main.ui.features.diary.library

import com.lazysyntax.nutron.models.Meal
import com.lazysyntax.nutron.models.Food

data class LibraryUiState(
    val barcode: String,
    val productName: String,
    val food: Food?,
    val foodList: List<Food>?,
    val meals: List<Meal>?
) {
    constructor() : this(
        barcode = "3017624010701",
        productName = "Nutella",
        food = null,
        foodList = emptyList(),
        meals = listOf(
            Meal(name = "Desayuno"),
            Meal(name = "Almuerzo"),
            Meal(name = "Cena")
        )
    )
}
