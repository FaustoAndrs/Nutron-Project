package com.lazysyntax.nutron.presentation.utilities.validation.validators

import com.lazysyntax.nutron.presentation.utilities.validation.Validation
import com.lazysyntax.nutron.presentation.utilities.validation.Validator
import org.jetbrains.compose.resources.StringResource

class NumberValidator(
    val error: StringResource
) : Validator<String> {
    override fun validate(data: String): Validation {
        return object : Validation {
            override val error: Boolean
                get() =
                    try {
                        data.toDouble().isNaN()
                    }catch (e: NumberFormatException){
                        true
                    }

            override val errorMessage: StringResource
                get() = this@NumberValidator.error
        }
    }
}
