package com.lazysyntax.nutron.main.utilities.validation.validadores

import com.lazysyntax.nutron.main.utilities.validation.Validation
import com.lazysyntax.nutron.main.utilities.validation.Validator

class MaximumTextLengthValidator(
    val maxLength: Int,
    val errorString: String = "El texto debe ser inferior o igual a $maxLength"
) : Validator<String> {
    override fun validate(data: String): Validation {
        return object : Validation {
            override val error: Boolean
                get() = data.length > maxLength
            override val errorMessage: String
                get() = errorString
        }
    }
}