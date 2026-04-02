package com.lazysyntax.nutron.main.ui.features.login.signUp

import com.lazysyntax.nutron.main.utilities.validation.Validation
import com.lazysyntax.nutron.main.utilities.validation.Validator
import com.lazysyntax.nutron.main.utilities.validation.ComposedValidator
import com.lazysyntax.nutron.main.utilities.validation.validadores.EmailValidator
import com.lazysyntax.nutron.main.utilities.validation.validadores.NonEmptyTextValidator

class SingUpValidator : Validator<SignUpUiState> {

    private val userNameValidator = ComposedValidator<String>()
        .add(NonEmptyTextValidator("El nombre de usuario no puede estar vacío."))

    private val fullNameValidator = ComposedValidator<String>()
        .add(NonEmptyTextValidator("El nombre completo no puede estar vacío."))

    private val emailValidator = ComposedValidator<String>()
        .add(NonEmptyTextValidator("El correo no puede estar vacío."))
        .add(EmailValidator("El formato del correo no es válido."))

    private val passwordValidator = ComposedValidator<String>()
        .add(NonEmptyTextValidator("La contraseña no puede estar vacía."))

    override fun validate(data: SignUpUiState): Validation {
        return SignUpUiStateValidation(
            userNameValidation = userNameValidator.validate(data.userName),
            fullNameValidation = fullNameValidator.validate(data.fullName),
            emailValidation = emailValidator.validate(data.email),
            passwordValidation = passwordValidator.validate(data.password)
        )
    }
}
