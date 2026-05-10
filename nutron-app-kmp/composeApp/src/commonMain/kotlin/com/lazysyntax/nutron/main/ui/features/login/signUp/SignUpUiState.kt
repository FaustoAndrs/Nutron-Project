package com.lazysyntax.nutron.main.ui.features.login.signUp

import com.lazysyntax.nutron.models.NewUser

data class SignUpUiState(
    val userName: String = "",
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val signUpSuccess: Boolean? = null,
    val errorMessage: String? = null
)

fun SignUpUiState.toNewUserEntity(): NewUser {
    return NewUser (
        userName = userName,
        fullName = fullName,
        email = email,
        password = password
    )
}
