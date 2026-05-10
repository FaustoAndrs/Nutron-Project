package com.lazysyntax.nutron.main.ui.features.settings

sealed interface SettingsEvent {
    object OnClickLanguage : SettingsEvent
    data class LanguageChanged(val lang: String) : SettingsEvent


}
