package com.lazysyntax.nutron.presentation.utilities.validation

interface Validator<T> {
    fun validate(data: T): Validation
}

