package com.lazysyntax.nutron.main.ui.features.diary

import com.lazysyntax.nutron.models.Food
import kotlinx.datetime.LocalDate

sealed interface DiaryEvent {
    object OnClickAddProduct : DiaryEvent
    // Date events
    object OnClickChangeDate : DiaryEvent
    object OnDismissDatePicker : DiaryEvent
    data class OnDateSelected(val dateMillis: Long?) : DiaryEvent
    object OnClickPreviousDay : DiaryEvent
    object OnClickNextDay : DiaryEvent
}
