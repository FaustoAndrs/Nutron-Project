package com.lazysyntax.nutron.main.utilities.validation

// ValidacionCompuesta.kt -----------------------------------------------
// Es una clase de utilidad que tiene una lista de validaciones que debemos pasar antes
// de dar por válidos los datos de un formulario.
// Rehusamos para ello los objetos que representan los estados de validación de cada campo y si alguno
// de ellos tiene error lo indicaremos en la propiedad calculada de solo lectura hayError de esta clase.
open class ComposedValidation : Validation {
    private val validations = mutableListOf<Validation>()
    fun add(validation: Validation): ComposedValidation {
        validations.add(validation)
        return this
    }

    override val error: Boolean
        get() = validations.any { it.error }

    override val errorMessage: String?
        get() = validations.firstOrNull { it.error }?.errorMessage
}