package com.lazysyntax.nutron.data.remote.authentication

sealed class AuthResult {
    object Success : AuthResult()
    object InvalidCredentials : AuthResult()
    object UserAlreadyExists : AuthResult() // 409 Conflict
    object NetworkError : AuthResult()
    data class UnknownError(val message: String?) : AuthResult()
}
