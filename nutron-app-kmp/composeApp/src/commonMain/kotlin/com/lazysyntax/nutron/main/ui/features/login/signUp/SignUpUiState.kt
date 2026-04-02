package com.lazysyntax.nutron.main.ui.features.login.signUp

data class SignUpUiState(
    val userName: String = "",
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val signUpSuccess: Boolean? = null,
    val errorMessage: String? = null
)
