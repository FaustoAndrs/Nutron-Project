package com.lazysyntax.nutron.main.utilities.validation.validadores

import com.lazysyntax.nutron.main.utilities.validation.Validation
import com.lazysyntax.nutron.main.utilities.validation.Validator
import org.jetbrains.compose.resources.StringResource

class EmailValidator(
    val error: StringResource
) : Validator<String> {
    override fun validate(data: String): Validation {
        return object : Validation {
            override val error: Boolean
                get() = if(!data.isBlank()) {
                    !Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})$").matches(data)
                } else {
                    false
                }
            override val errorMessage: StringResource
                get() = this@EmailValidator.error
        }
    }
}