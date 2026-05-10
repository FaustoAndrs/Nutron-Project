package com.lazysyntax.nutron.main.ui.features.profile

data class ProfileUiState(
    val weight: String = "",
    val height: String = "",
    val gender: String = "",
    val basalMetabolicRate: String = "",
    val bodyMassIndex: String = "",
    val bodyFatPercentage: String = "",
    val bodyWaterPercentage: String = "",
    val activityFactor: String = "",
    val energeticBalance: String = ""
    )
