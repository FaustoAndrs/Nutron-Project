package com.lazysyntax.nutron.main.ui.features.login.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.lazysyntax.nutron.main.ui.composables.TextFieldEmail
import com.lazysyntax.nutron.main.ui.composables.TextFieldPassword
import com.lazysyntax.nutron.main.utilities.validation.Validation


@Composable
fun UsuarioPassword(
    modifier: Modifier,
    loginState: String,
    validacionLogin: Validation,
    passwordState: String,
    validacionPassword: Validation,
    recordarmeState: Boolean,
    onValueChangeLogin: (String) -> Unit,
    onValueChangePassword: (String) -> Unit,
    onCheckedChanged: (Boolean) -> Unit,
    onClickLogin: () -> Unit
) {
    Column {
        TextFieldEmail(
            modifier = modifier,
            label = "Login",
            emailState = loginState,
            validacionState = validacionLogin,
            onValueChange = onValueChangeLogin
        )

        TextFieldPassword(
            modifier = modifier,
            label = "Password",
            passwordState = passwordState,
            validacionState = validacionPassword,
            onValueChange = onValueChangePassword
        )

        Button(
            onClick = onClickLogin,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}
