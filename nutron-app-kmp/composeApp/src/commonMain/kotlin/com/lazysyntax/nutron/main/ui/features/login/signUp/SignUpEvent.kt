package com.lazysyntax.nutron.main.ui.features.login.signUp

sealed interface SignUpEvent {
    data class UserNameChanged(val userName: String) : SignUpEvent
    data class FullNameChanged(val fullName: String) : SignUpEvent
    data class EmailChanged(val email: String) : SignUpEvent
    data class PasswordChanged(val password: String) : SignUpEvent
    object OnClickSignUp : SignUpEvent
    object OnSignUpSuccess : SignUpEvent
    object OnBack : SignUpEvent

}
