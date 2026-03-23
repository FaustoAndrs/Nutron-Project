package com.lazysyntax.nutron.main.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.lazysyntax.nutron.main.utilities.validation.Validation
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.no_empty_text_string_validation
import org.jetbrains.compose.resources.stringResource


@Composable
fun TextFieldWithErrorState(
    modifier: Modifier = Modifier,
    textoState: String,
    textoPista: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    validacionState: Validation,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = modifier,
        value = textoState,
        onValueChange = onValueChange,
        singleLine = true,
        leadingIcon = leadingIcon,
        placeholder = {
            Text(
                text = textoPista,
                style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
            )
        },
        keyboardOptions = keyboardOptions,
        supportingText = {
            if (validacionState.error) {
                Text(text = validacionState.errorMessage!!)
            }
        },
        isError = validacionState.error,
        //keyboardActions = keyboardActions,
        colors = TextFieldDefaults.colors(
            //TODO("Refactorizar colores")
            //BrightLightGrayVp.copy(alpha = 0.5f),
            focusedContainerColor = Color.LightGray.copy(alpha = 0.5f),
            unfocusedContainerColor = Color.LightGray,
            errorPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
            //LighBlueVp
            focusedIndicatorColor = Color.Cyan,
            cursorColor = Color.Cyan
        ),
    )
}


@Composable
fun OutlinedTextAreaWithErrorState(
    modifier: Modifier = Modifier,
    label: String = "",
    textoState: String,
    textoPista: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    validacionState: Validation,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = 10,
    lineheight: Dp = 24.dp,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .background(color = Color.Red)
            .fillMaxWidth()
            .height(maxLines * lineheight),
        value = textoState,
        onValueChange = onValueChange,
        singleLine = singleLine,
        leadingIcon = leadingIcon,
        placeholder = {
            Text(
                text = textoPista,
                style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
            )
        },
        keyboardOptions = keyboardOptions,
        supportingText = {
            if (validacionState.error) {
                Text(
                    modifier = Modifier.background(color = Color.Red),
                    text = stringResource(Res.string.no_empty_text_string_validation)

                )
            }
        },
        isError = validacionState.error,
        keyboardActions = keyboardActions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.LightGray.copy(alpha = 0.5f),
            unfocusedContainerColor = Color.LightGray,
            focusedBorderColor = Color.Cyan,
            cursorColor = Color.Cyan,
        )
    )
}

@Composable
fun TextFieldPhone(
    modifier: Modifier = Modifier,
    label: String = "",
    telefonoState: String,
    validacionState: Validation,
    onValueChange: (String) -> Unit
) {
    TextFieldWithErrorState(
        modifier = modifier,
        textoState = telefonoState,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        validacionState = validacionState,
        onValueChange = onValueChange,
        textoPista = label,

    )
}


@Composable
fun TextFieldEmail(
    modifier: Modifier = Modifier,
    label: String = "",
    emailState: String,
    validacionState: Validation,
    onValueChange: (String) -> Unit,
    //keyboardActions: KeyboardActions = null
) {

    TextFieldWithErrorState(
        modifier = modifier,
        textoState = emailState,
        textoPista = label,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        validacionState = validacionState,
        onValueChange = onValueChange,
        //keyboardActions = keyboardActions
    )
}


@Composable
fun TextFieldPassword(
    modifier: Modifier = Modifier,
    passwordState: String,
    validacionState: Validation,
    onValueChange: (String) -> Unit,
    label: String = "Clave",
    labelShow: String = "Muestra clave",
    labelHide: String = "Oculta clave",
    iconoInformativo: Painter = rememberVectorPainter(image = Icons.Filled.Lock),
) {
    var passwordHidden by remember { mutableStateOf(true) }
    TextField(
        modifier = modifier,
        value = passwordState,
        onValueChange = onValueChange,
        singleLine = true,
        label = { Text(if (validacionState.error) "${label}*" else label) },
        supportingText = {
            if (validacionState.error) {
                Text(text = validacionState.errorMessage!!)
            }
        },
        isError = validacionState.error,
        visualTransformation =
            if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        leadingIcon = {
            Icon(
                painter = iconoInformativo,
                contentDescription = label
            )
        },
        trailingIcon = {
            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                val visibilityIcon =
                    if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordHidden) labelShow else labelHide
                Icon(imageVector = visibilityIcon, contentDescription = description)
            }
        }
    )
}

@Composable
fun OutlinedTextFieldPassword(
    modifier: Modifier = Modifier,
    passwordState: String,
    validacionState: Validation,
    onValueChange: (String) -> Unit,
    label: String = "Clave",
    labelShow: String = "Muestra clave",
    labelHide: String = "Oculta clave",
    iconoInformativo: Painter = rememberVectorPainter(image = Icons.Filled.Lock),
) {
    var passwordHidden by remember { mutableStateOf(true) }
    OutlinedTextField(
        modifier = modifier,
        value = passwordState,
        onValueChange = onValueChange,
        singleLine = true,
        label = { Text(if (validacionState.error) "${label}*" else label) },
        supportingText = {
            if (validacionState.error) {
                Text(text = validacionState.errorMessage!!)
            }
        },
        isError = validacionState.error,
        visualTransformation =
            if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        leadingIcon = {
            Icon(
                painter = iconoInformativo,
                contentDescription = label
            )
        },
        trailingIcon = {
            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                val visibilityIcon =
                    if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordHidden) labelShow else labelHide
                Icon(imageVector = visibilityIcon, contentDescription = description)
            }
        }
    )
}

