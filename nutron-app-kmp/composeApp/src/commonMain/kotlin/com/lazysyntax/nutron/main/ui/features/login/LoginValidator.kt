package com.lazysyntax.nutron.main.ui.features.login

import com.lazysyntax.nutron.main.utilities.validation.Validation
import com.lazysyntax.nutron.main.utilities.validation.Validator
import com.lazysyntax.nutron.main.utilities.validation.ComposedValidator
import com.lazysyntax.nutron.main.utilities.validation.validadores.EmailValidator
import com.lazysyntax.nutron.main.utilities.validation.validadores.MaximumTextLengthValidator
import com.lazysyntax.nutron.main.utilities.validation.validadores.PhoneValidator
import com.lazysyntax.nutron.main.utilities.validation.validadores.NonEmptyTextValidator


class LoginValidator() : Validator<LoginUiState> {
    val validadorDescripcion = ComposedValidator<String>()
        .add(NonEmptyTextValidator("El campo de descripción no puede estar vacío."))
        .add(MaximumTextLengthValidator(500))

    val validadorEmail = ComposedValidator<String>()
        .add(EmailValidator("La dirección de correo no es válido"))

    override fun validate(data: LoginUiState): Validation {
        val validacionDescripcion = validadorDescripcion.validate(data.email)
        val validacionEmail = validadorEmail.validate(data.password)

        return LoginUiStateValidation(
            validacionDescripcion = validacionDescripcion,
            validacionEmail = validacionEmail,
        )
    }
}