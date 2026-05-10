package com.lazysyntax.nutron.main.utilities.validation.validadores

import com.lazysyntax.nutron.main.utilities.validation.Validation
import com.lazysyntax.nutron.main.utilities.validation.Validator
import org.jetbrains.compose.resources.StringResource

class PhoneValidator(
    val error: StringResource
) : Validator<String> {
    override fun validate(data: String): Validation {
        return object : Validation {
            override val error: Boolean
                get() = if(!data.isBlank()) {
                     !Regex("^\\+?[0-9 ]{9,18}$").matches(data)
                } else {
                    false
                }
            override val errorMessage: StringResource
                get() = this@PhoneValidator.error
        }
    }
}
