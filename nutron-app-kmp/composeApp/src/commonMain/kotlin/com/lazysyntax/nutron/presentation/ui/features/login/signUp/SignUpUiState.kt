package com.lazysyntax.nutron.presentation.ui.features.login.signUp

import com.lazysyntax.nutron.domain.models.User
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class SignUpUiState(
    val userName: String = "",
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val signUpSuccess: Boolean? = null,
    val errorMessage: String? = null
)

@OptIn(ExperimentalUuidApi::class)
fun SignUpUiState.toNewUserEntity(): User {
    return User (
        id = Uuid.random().toString(),
        userName = userName,
        fullName = fullName,
        email = email,
        password = password
    )
}
