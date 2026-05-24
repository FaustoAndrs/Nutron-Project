package com.lazysyntax.nutron.presentation.ui.features.login

data class LoginUiState(
    val email: String = "usu01@mail.com",
    val password: String = "123",
    val isLoading: Boolean = false,
    val loginSuccess: Boolean? = null,
    val errorMessage: String? = null
)
