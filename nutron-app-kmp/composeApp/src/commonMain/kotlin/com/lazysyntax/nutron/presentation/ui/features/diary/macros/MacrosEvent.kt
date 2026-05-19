package com.lazysyntax.nutron.presentation.ui.features.diary.macros

sealed interface MacrosEvent {
    object OnClickBack : MacrosEvent
    data class QuantityChanged(val quantity: String) : MacrosEvent
    object  OnclickSave : MacrosEvent

}
