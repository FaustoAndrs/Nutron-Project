package com.lazysyntax.nutron.main.ui.features.login

import com.lazysyntax.nutron.main.utilities.validation.Validation
import com.lazysyntax.nutron.main.utilities.validation.Validator
import com.lazysyntax.nutron.main.utilities.validation.ComposedValidator
import com.lazysyntax.nutron.main.utilities.validation.validadores.EmailValidator
import com.lazysyntax.nutron.main.utilities.validation.validadores.NonEmptyTextValidator
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.email_no_empty_validator
import nutron.composeapp.generated.resources.email_validator
import nutron.composeapp.generated.resources.password_no_empty_validator

class LoginValidator : Validator<LoginUiState> {
    private val emailValidator = ComposedValidator<String>()
        .add(NonEmptyTextValidator(Res.string.email_no_empty_validator))
        .add(EmailValidator(Res.string.email_validator))

    private val passwordValidator = ComposedValidator<String>()
        .add(NonEmptyTextValidator(Res.string.password_no_empty_validator))

    override fun validate(data: LoginUiState): Validation {
        return LoginUiStateValidation(
            emailValidation = emailValidator.validate(data.email),
            passwordValidation = passwordValidator.validate(data.password)
        )
    }
}
