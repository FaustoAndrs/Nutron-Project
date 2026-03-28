package com.lazysyntax.nutron.main.ui.features.login

import com.lazysyntax.nutron.main.utilities.validation.Validation
import com.lazysyntax.nutron.main.utilities.validation.Validator
import com.lazysyntax.nutron.main.utilities.validation.ComposedValidator
import com.lazysyntax.nutron.main.utilities.validation.validadores.EmailValidator
import com.lazysyntax.nutron.main.utilities.validation.validadores.NonEmptyTextValidator

class LoginValidator : Validator<LoginUiState> {
    private val emailValidator = ComposedValidator<String>()
        .add(NonEmptyTextValidator("El correo no puede estar vacío."))
        .add(EmailValidator("El formato del correo no es válido."))

    private val passwordValidator = ComposedValidator<String>()
        .add(NonEmptyTextValidator("La contraseña no puede estar vacía."))

    override fun validate(data: LoginUiState): Validation {
        return LoginUiStateValidation(
            emailValidation = emailValidator.validate(data.email),
            passwordValidation = passwordValidator.validate(data.password)
        )
    }
}
