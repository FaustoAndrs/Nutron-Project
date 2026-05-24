package com.lazysyntax.nutron.presentation.ui.features.settings

sealed interface SettingsEvent {
    object OnClickLanguage : SettingsEvent
    data class LanguageChanged(val lang: String) : SettingsEvent
    data class OnToggleDarkTheme(val enabled: Boolean) : SettingsEvent
    object GenerateTestData : SettingsEvent
    object OnClickSync : SettingsEvent
    object OnClickLogOut: SettingsEvent
    object OnConfirmLogOut: SettingsEvent
    object OnDismissLogOut: SettingsEvent
    object  OnCreateAccount: SettingsEvent
    object  OnConfirmAdviseCreateAccount: SettingsEvent

}
