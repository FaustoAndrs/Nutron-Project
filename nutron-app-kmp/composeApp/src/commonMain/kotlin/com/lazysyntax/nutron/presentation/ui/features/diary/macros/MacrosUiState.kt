package com.lazysyntax.nutron.presentation.ui.features.diary.macros

import com.lazysyntax.nutron.domain.models.Food

data class MacrosUiState(
    val food: Food? = null,
    val editedFood: Food = food ?: Food()
)
