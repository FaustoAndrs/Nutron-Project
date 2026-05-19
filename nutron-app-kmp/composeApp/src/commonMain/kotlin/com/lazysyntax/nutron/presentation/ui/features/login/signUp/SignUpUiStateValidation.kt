package com.lazysyntax.nutron.presentation.ui.features.login.signUp

import com.lazysyntax.nutron.presentation.utilities.validation.Validation
import com.lazysyntax.nutron.presentation.utilities.validation.ComposedValidation
import org.jetbrains.compose.resources.StringResource

data class SignUpUiStateValidation(
    val userNameValidation: Validation = object : Validation {},
    val fullNameValidation: Validation = object : Validation {},
    val emailValidation: Validation = object : Validation {},
    val passwordValidation: Validation = object : Validation {},
) : Validation {
    private var validacionCompuesta: ComposedValidation? = null

    private fun componerValidacion(): ComposedValidation {
        val composed = ComposedValidation()
            .add(userNameValidation)
            .add(fullNameValidation)
            .add(emailValidation)
            .add(passwordValidation)
        validacionCompuesta = composed
        return composed
    }

    override val error: Boolean
        get() = validacionCompuesta?.error ?: componerValidacion().error

    override val errorMessage: StringResource?
        get() = validacionCompuesta?.errorMessage ?: componerValidacion().errorMessage
}
