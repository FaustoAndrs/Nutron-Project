package com.lazysyntax.nutron.main.ui.features.diary

import com.lazysyntax.nutron.models.Meal
import com.lazysyntax.nutron.models.Food
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock


data class DiaryUiState(
    val date: LocalDate,
    val isDatePickerVisible: Boolean = false,
    val barcode: String,
    val productName: String,
    val food: Food?,
    val foodList: List<Food>?,
    val meals: List<Meal>?
) {
    constructor() : this(
        date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
        isDatePickerVisible = false,
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
