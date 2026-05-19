package com.lazysyntax.nutron.presentation.ui.features.targets


import com.lazysyntax.nutron.presentation.ui.features.targets.composables.Diet
import kotlinx.serialization.Serializable

@Serializable
data class TargetsUiState(
    val dailyKcal: String = "",
    val diet: Diet = Diet("Standard", 50, 20, 30)

)
