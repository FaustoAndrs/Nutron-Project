package com.lazysyntax.nutron.main.utilities.validation.validadores

import com.lazysyntax.nutron.main.utilities.validation.Validation
import com.lazysyntax.nutron.main.utilities.validation.Validator
import org.jetbrains.compose.resources.StringResource

class MinTextLengthValidator(
    val minLength: Int,
    val error: StringResource
) : Validator<String> {
    override fun validate(data: String): Validation {
        return object : Validation {
            override val error: Boolean
                get() = data.length < minLength
            override val errorMessage: StringResource
                get() = this@MinTextLengthValidator.error
        }
    }
}
