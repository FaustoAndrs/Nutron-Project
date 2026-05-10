package com.lazysyntax.nutron.main.ui.features.login.signUp

import com.lazysyntax.nutron.main.utilities.validation.Validation
import com.lazysyntax.nutron.main.utilities.validation.Validator
import com.lazysyntax.nutron.main.utilities.validation.ComposedValidator
import com.lazysyntax.nutron.main.utilities.validation.validadores.EmailValidator
import com.lazysyntax.nutron.main.utilities.validation.validadores.NonEmptyTextValidator
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.email_no_empty_validator
import nutron.composeapp.generated.resources.email_validator
import nutron.composeapp.generated.resources.full_name_no_empty_validator
import nutron.composeapp.generated.resources.password_no_empty_validator
import nutron.composeapp.generated.resources.user_name_no_empty_validator

class SingUpValidator : Validator<SignUpUiState> {

    private val userNameValidator = ComposedValidator<String>()
        .add(NonEmptyTextValidator(Res.string.user_name_no_empty_validator))

    private val fullNameValidator = ComposedValidator<String>()
        .add(NonEmptyTextValidator(Res.string.full_name_no_empty_validator))

    private val emailValidator = ComposedValidator<String>()
        .add(NonEmptyTextValidator(Res.string.email_no_empty_validator))
        .add(EmailValidator(Res.string.email_validator))

    private val passwordValidator = ComposedValidator<String>()
        .add(NonEmptyTextValidator(Res.string.password_no_empty_validator))

    override fun validate(data: SignUpUiState): Validation {
        return SignUpUiStateValidation(
            userNameValidation = userNameValidator.validate(data.userName),
            fullNameValidation = fullNameValidator.validate(data.fullName),
            emailValidation = emailValidator.validate(data.email),
            passwordValidation = passwordValidator.validate(data.password)
        )
    }
}
