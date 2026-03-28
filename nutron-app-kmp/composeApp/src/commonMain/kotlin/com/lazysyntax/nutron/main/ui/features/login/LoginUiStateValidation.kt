package com.lazysyntax.nutron.main.ui.features.login

import com.lazysyntax.nutron.main.utilities.validation.Validation
import com.lazysyntax.nutron.main.utilities.validation.ComposedValidation

data class LoginUiStateValidation(
    val emailValidation: Validation = object : Validation {},
    val passwordValidation: Validation = object : Validation {},
) : Validation {
    private var validacionCompuesta: ComposedValidation? = null

    private fun componerValidacion(): ComposedValidation {
        val composed = ComposedValidation()
            .add(emailValidation)
            .add(passwordValidation)
        validacionCompuesta = composed
        return composed
    }

    override val error: Boolean
        get() = validacionCompuesta?.error ?: componerValidacion().error

    override val errorMessage: String?
        get() = validacionCompuesta?.errorMessage ?: componerValidacion().errorMessage
}
