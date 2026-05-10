package com.lazysyntax.nutron.data.services.authentication

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)