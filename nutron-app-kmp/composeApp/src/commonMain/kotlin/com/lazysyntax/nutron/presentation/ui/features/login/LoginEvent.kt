package com.lazysyntax.nutron.presentation.ui.features.login

sealed interface LoginEvent {
    data class EmailChanged(val login: String) : LoginEvent
    data class PasswordChanged(val password: String) : LoginEvent
    object OnClickLogin :LoginEvent
    object OnClickSignUp :LoginEvent
    object OnClickSkipLogin :LoginEvent
    object OnLoginSuccess : LoginEvent


}
