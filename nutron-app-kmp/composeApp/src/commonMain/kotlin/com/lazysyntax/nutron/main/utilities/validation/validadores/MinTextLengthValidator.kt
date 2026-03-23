package com.lazysyntax.nutron.main.utilities.validation.validadores

import com.lazysyntax.nutron.main.utilities.validation.Validation
import com.lazysyntax.nutron.main.utilities.validation.Validator

class MinTextLengthValidator(
    val minLength: Int,
    val error: String = "El texto debe mayor o igual a ${minLength}"
) : Validator<String> {
    override fun validate(data: String): Validation {
        return object : Validation {
            override val error: Boolean
                get() = data.length < minLength
            override val errorMessage: String
                get() = this@MinTextLengthValidator.error
        }
    }
}
