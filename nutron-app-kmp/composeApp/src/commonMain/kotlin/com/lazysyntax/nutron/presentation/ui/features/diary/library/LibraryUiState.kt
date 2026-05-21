package com.lazysyntax.nutron.presentation.ui.features.diary.library

import com.lazysyntax.nutron.domain.models.Food
import com.lazysyntax.nutron.presentation.ui.features.diary.DiaryViewModel.SearchSource

data class LibraryUiState(
    val barcode: String,
    val productName: String,
    val foodResult: Food?,
    val foodListResult: List<Food>?,
    val foodSelected: Food? = null,
    val searchSource: SearchSource = SearchSource.LOCAL,
    val error: String = ""


) {
    constructor() : this(
        barcode = "3017624010701",
        productName = "Nutella",
        foodResult = null,
        foodListResult = emptyList(),
        searchSource = SearchSource.LOCAL
    )
}