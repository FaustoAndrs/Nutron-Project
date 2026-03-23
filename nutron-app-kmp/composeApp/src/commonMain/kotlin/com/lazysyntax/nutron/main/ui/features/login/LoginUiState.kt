package com.lazysyntax.nutron.main.ui.features.login

data class LoginUiState(
    val email: String,
    val password: String,
) {
    constructor() : this(
        email = "",
        password = "",
    )
}