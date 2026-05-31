package com.lazysyntax.nutron.presentation.utilities.validation

import org.jetbrains.compose.resources.StringResource

open class ComposedValidation : Validation {
    private val validations = mutableListOf<Validation>()
    fun add(validation: Validation): ComposedValidation {
        validations.add(validation)
        return this
    }

    override val error: Boolean
        get() = validations.any { it.error }

    override val errorMessage: StringResource?
        get() = validations.firstOrNull { it.error }?.errorMessage
}