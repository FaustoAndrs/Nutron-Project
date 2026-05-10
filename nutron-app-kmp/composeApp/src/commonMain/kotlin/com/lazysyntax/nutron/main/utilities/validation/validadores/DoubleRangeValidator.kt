package com.lazysyntax.nutron.main.utilities.validation.validadores

import com.lazysyntax.nutron.main.utilities.validation.Validation
import com.lazysyntax.nutron.main.utilities.validation.Validator
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

class DoubleRangeValidator(
    val min: Double,
    val max: Double,
    val error: StringResource
) : Validator<String> {
    override fun validate(data: String): Validation {
        return object : Validation {
            override val error: Boolean
                get() = try {
                    data.toDouble() !in min..max
                } catch (e: NumberFormatException) {
                    true
                }

            override val errorMessage: StringResource
                get() = this@DoubleRangeValidator.error
        }
    }
}
