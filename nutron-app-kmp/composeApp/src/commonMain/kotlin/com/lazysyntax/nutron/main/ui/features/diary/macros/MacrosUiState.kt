package com.lazysyntax.nutron.main.ui.features.diary.macros

import com.lazysyntax.nutron.models.Food
import com.lazysyntax.nutron.models.Meal

data class MacrosUiState(
    val barcode: String,
    val productName: String,
    val food: Food?,
    val meals: Meal?
) {
    constructor() : this(
        barcode = "3017624010701",
        productName = "Nutella",
        food = null,
        meals = null
    )
}