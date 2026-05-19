package com.lazysyntax.nutron.presentation.utilities.validation.validators

import com.lazysyntax.nutron.presentation.utilities.validation.Validation
import com.lazysyntax.nutron.presentation.utilities.validation.Validator
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.weight_no_empty_validator
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.StringResource

@OptIn(InternalResourceApi::class)
class NonEmptyTextValidator (
    val error: StringResource  = Res.string.weight_no_empty_validator
) : Validator<String> {
    override fun validate(data: String): Validation {
        return object : Validation {
            override val error: Boolean
                get() = data.isBlank()
            override val errorMessage: StringResource
                get() = this@NonEmptyTextValidator.error
        }
    }
}

