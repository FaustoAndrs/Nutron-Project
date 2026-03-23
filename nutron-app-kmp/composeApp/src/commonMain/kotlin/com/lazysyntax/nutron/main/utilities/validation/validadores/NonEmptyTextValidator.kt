package com.lazysyntax.nutron.main.utilities.validation.validadores

import com.lazysyntax.nutron.main.utilities.validation.Validation
import com.lazysyntax.nutron.main.utilities.validation.Validator


class NonEmptyTextValidator(
    val error: String = "El campo no puede estar vacío"
) : Validator<String> {
    override fun validate(data: String): Validation {
        return object : Validation {
            override val error: Boolean
                get() = data.isBlank()
            override val errorMessage: String
                get() = this@NonEmptyTextValidator.error
        }
    }
}
