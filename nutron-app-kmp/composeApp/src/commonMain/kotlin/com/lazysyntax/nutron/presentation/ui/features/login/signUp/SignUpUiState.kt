package com.lazysyntax.nutron.presentation.ui.features.login.signUp

import com.lazysyntax.nutron.domain.models.User

data class SignUpUiState(
    val userName: String = "",
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val signUpSuccess: Boolean? = null,
    val errorMessage: String? = null
)

fun SignUpUiState.toNewUserEntity(): User {
    return User (
        userName = userName,
        fullName = fullName,
        email = email,
        password = password
    )
}
