package com.lazysyntax.nutron.presentation.ui.features.diary

import com.lazysyntax.nutron.presentation.ui.features.diary.library.LibraryUiState
import com.lazysyntax.nutron.presentation.ui.features.diary.macros.MacrosUiState
import com.lazysyntax.nutron.presentation.ui.features.targets.TargetsUiState
import com.lazysyntax.nutron.domain.models.Meal
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock


data class DiaryUiState(
    val date: LocalDate, // Fecha actual
    val isDatePickerVisible: Boolean = false,
    val meals: List<Meal>? = null, // Lista de comidas para la fecha actual
    val selectedMeal: Meal? = null, // Comida seleccionada para agregar productos
    val targets: TargetsUiState = TargetsUiState(),

    val libraryUiState: LibraryUiState = LibraryUiState(),
    val macrosUiState: MacrosUiState = MacrosUiState(
        libraryUiState.foodSelected,

    )
) {
    constructor() : this(
        date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    )


}
