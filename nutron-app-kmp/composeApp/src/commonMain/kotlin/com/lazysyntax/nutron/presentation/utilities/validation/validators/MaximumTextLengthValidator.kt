package com.lazysyntax.nutron.presentation.utilities.validation.validators

import com.lazysyntax.nutron.presentation.utilities.validation.Validation
import com.lazysyntax.nutron.presentation.utilities.validation.Validator
import org.jetbrains.compose.resources.StringResource

class MaximumTextLengthValidator(
    val maxLength: Int,
    val error: StringResource
) : Validator<String> {
    override fun validate(data: String): Validation {
        return object : Validation {
            override val error: Boolean
                get() = data.length > maxLength
            override val errorMessage: StringResource
                get() = this@MaximumTextLengthValidator.error
        }
    }
}