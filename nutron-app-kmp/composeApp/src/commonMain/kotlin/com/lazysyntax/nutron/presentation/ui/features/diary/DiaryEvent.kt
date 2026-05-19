package com.lazysyntax.nutron.presentation.ui.features.diary

import com.lazysyntax.nutron.domain.models.Meal

sealed interface DiaryEvent {
    data class OnClickAddProduct(val meal: Meal) : DiaryEvent
    // Date events
    object OnClickChangeDate : DiaryEvent
    object OnDismissDatePicker : DiaryEvent
    data class OnDateSelected(val dateMillis: Long?) : DiaryEvent
    object OnClickPreviousDay : DiaryEvent
    object OnClickNextDay : DiaryEvent
    data class OnAddMeal(val meal: Meal) : DiaryEvent
    data class OnDeleteMeal(val meal: Meal) : DiaryEvent


}
