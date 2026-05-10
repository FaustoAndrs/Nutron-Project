package com.lazysyntax.nutron.main.ui.features.login

data class LoginUiState(
    val email: String = "f@mail.com",
    val password: String = "1234",
    val isLoading: Boolean = false,
    val loginSuccess: Boolean? = null,
    val errorMessage: String? = null
)
