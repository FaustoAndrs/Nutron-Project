package com.lazysyntax.nutron.presentation.ui.features.settings

data class SettingsUiState(
    val language: String = "es",
    val isDarkTheme: Boolean = false,
    val unsyncedMealsCount: Int = 0,
    val showMealsCountAdvise: Boolean = true,
    val isSyncing: Boolean = false,
    val isGeneratingTestData: Boolean = false,
    val showLogoutConfirmation: Boolean = false,
    val showAdviseCreateAccount: Boolean = false,
    val isGuestLogged: Boolean = false
)
