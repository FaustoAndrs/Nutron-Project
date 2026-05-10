package com.lazysyntax.nutron.main.ui.features.targets


import com.lazysyntax.nutron.main.ui.features.targets.composables.Diet
import kotlinx.serialization.Serializable

@Serializable
data class TargetsUiState(
    val dailyKcal: String = "",
    val diet : Diet =  Diet("Standard", 50, 20, 30)

)
