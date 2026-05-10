package com.lazysyntax.nutron.main.ui.features.diary.library

import com.lazysyntax.nutron.models.Food

sealed interface LibraryEvent {
    data class ProductNameChanged(val productName: String) : LibraryEvent
    data class BarcodeChanged(val barcode: String) : LibraryEvent
    object OnClickSearchBarcode : LibraryEvent
    object OnClickSearchProduct : LibraryEvent
    data class ProductSelected(val product: Food) : LibraryEvent
    object OnClickBack : LibraryEvent

}
