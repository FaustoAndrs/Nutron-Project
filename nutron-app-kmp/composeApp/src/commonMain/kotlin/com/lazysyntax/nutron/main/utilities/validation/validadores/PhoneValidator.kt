package com.lazysyntax.nutron.main.utilities.validation.validadores

import com.lazysyntax.nutron.main.utilities.validation.Validation
import com.lazysyntax.nutron.main.utilities.validation.Validator

class PhoneValidator(
    val error: String = "Teléfono no válido"
) : Validator<String> {
    override fun validate(data: String): Validation {
        return object : Validation {
            override val error: Boolean
                get() = if(!data.isBlank()) {
                     !Regex("^\\+?[0-9 ]{9,18}$").matches(data)
                } else {
                    false
                }
            override val errorMessage: String
                get() = this@PhoneValidator.error
        }
    }
}
