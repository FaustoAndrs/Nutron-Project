package com.lazysyntax.nutron.presentation.ui.features.diary.library

import com.lazysyntax.nutron.domain.models.Food
import com.lazysyntax.nutron.domain.models.Meal

sealed interface LibraryEvent {
    data class ProductNameChanged(val productName: String) : LibraryEvent
    data class BarcodeChanged(val barcode: String) : LibraryEvent
    object OnClickSearchBarcode : LibraryEvent
    object OnClickSearchProduct : LibraryEvent
    data class ProductSelected(val product: Food) : LibraryEvent
    data class SelectedMeal(val meal: Meal) : LibraryEvent
    object OnClickBack : LibraryEvent

}
