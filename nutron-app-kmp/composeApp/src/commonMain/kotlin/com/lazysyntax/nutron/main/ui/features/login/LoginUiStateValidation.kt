package com.lazysyntax.nutron.main.ui.features.login

import com.lazysyntax.nutron.main.utilities.validation.Validation
import com.lazysyntax.nutron.main.utilities.validation.ComposedValidation

data class LoginUiStateValidation(
    val validacionDescripcion: Validation = object : Validation {},
    val validacionEmail: Validation = object : Validation {},
    val validacionTelefono: Validation = object : Validation {},

    ) : Validation {
    private var validacionCompuesta: ComposedValidation? = null

    private fun componerValidacion(): ComposedValidation {
        validacionCompuesta = ComposedValidation()
            .add(validacionDescripcion)
            .add(validacionEmail)
            .add(validacionTelefono)
        return validacionCompuesta!!
    }


    override val error: Boolean
        get() = validacionCompuesta?.error ?: componerValidacion().error

    override val errorMessage: String?
        get() = validacionCompuesta?.errorMessage ?: componerValidacion().errorMessage

}