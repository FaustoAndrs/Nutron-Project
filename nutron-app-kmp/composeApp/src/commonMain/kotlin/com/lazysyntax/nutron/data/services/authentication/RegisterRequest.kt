package com.lazysyntax.nutron.data.services.authentication

data class RegisterRequest(
    val username: String,
    val fullname: String,
    val email: String,
    val password: String,

    )
