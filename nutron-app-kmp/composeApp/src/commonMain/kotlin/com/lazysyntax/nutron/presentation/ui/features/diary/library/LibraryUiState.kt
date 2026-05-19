package com.lazysyntax.nutron.presentation.ui.features.diary.library

import com.lazysyntax.nutron.domain.models.Food

data class LibraryUiState(
    val barcode: String,
    val productName: String,
    val foodResult: Food?,
    val foodListResult: List<Food>?,
    val foodSelected: Food? = null
) {
    constructor() : this(
        barcode = "3017624010701",
        productName = "Nutella",
        foodResult = null,
        foodListResult = emptyList(),
    )
}
