package com.lazysyntax.nutron.main.ui.features.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val loginSuccess: Boolean? = null,
    val errorMessage: String? = null
)
