package com.lazysyntax.nutron.data.services.authentication

import com.lazysyntax.nutron.models.NewUser
import com.lazysyntax.nutron.models.UserSetup
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username: String,
    val fullname: String,
    val email: String,
    val password: String,
    val userSetup: UserSetup? = null
)