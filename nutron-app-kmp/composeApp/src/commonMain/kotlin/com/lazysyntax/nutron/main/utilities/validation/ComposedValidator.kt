package com.lazysyntax.nutron.main.utilities.validation

// ValidadorCompuesto.kt -----------------------------------------------
// Implementa la interfaz Validador y es una clase de utilidad que tiene
// una lista de validadores que debemos pasar para un conjunto de datos.
// Por ejemplo para un teléfono, podemos tener una validación que compruebe
// que no está vacío, otra que compruebe que tiene una longitud mínima.
// Nota: Las validaciones se ejecutan en orden y si alguna de ellas tiene error
// se devuelve el error y no se ejecutan las siguientes.
open class ComposedValidator<T> : Validator<T> {
    private val validators = mutableListOf<Validator<T>>()

    fun add(validator: Validator<T>): ComposedValidator<T> {
        validators.add(validator)
        return this
    }

    override fun validate(data: T): Validation =
        validators
            .map { it.validate(data) }
            .firstOrNull { it.error }
            ?: object : Validation {}
}
