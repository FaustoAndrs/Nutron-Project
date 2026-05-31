package com.lazysyntax.nutron.presentation.utilities.validation

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
