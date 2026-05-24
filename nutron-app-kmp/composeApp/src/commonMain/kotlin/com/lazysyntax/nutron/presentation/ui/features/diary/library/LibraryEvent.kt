package com.lazysyntax.nutron.presentation.ui.features.diary.library

import com.lazysyntax.nutron.domain.models.Food
import com.lazysyntax.nutron.domain.models.Meal
import com.lazysyntax.nutron.presentation.ui.features.diary.DiaryViewModel.SearchSource

sealed interface LibraryEvent {
    data class ProductNameChanged(val productName: String) : LibraryEvent
    data class BarcodeChanged(val barcode: String) : LibraryEvent
    data class SearchSourceChanged(val source: SearchSource) : LibraryEvent
    object OnClickSearchBarcode : LibraryEvent
    object OnClickSearchProduct : LibraryEvent
    data class ProductSelected(val product: Food) : LibraryEvent
    data class SelectedMeal(val meal: Meal) : LibraryEvent
    data class OnError(val message: String) : LibraryEvent
    object OnClickBack : LibraryEvent

}