package com.lazysyntax.nutron.presentation.ui.features.profile

import com.lazysyntax.nutron.presentation.ui.features.setUp.Gender

data class ProfileUiState(
    val weight: String = "",
    val height: String = "",
    val gender: Gender,
    val basalMetabolicRate: String = "",
    val bodyMassIndex: String = "",
    val bodyFatPercentage: String = "",
    val bodyWaterPercentage: String = "",
    val gastoEnergeticoTotal: String = "",
    val energeticBalance: String = ""
    )
