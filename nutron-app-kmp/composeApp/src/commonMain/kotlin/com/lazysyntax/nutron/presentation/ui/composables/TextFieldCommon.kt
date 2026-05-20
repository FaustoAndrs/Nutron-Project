package com.lazysyntax.nutron.presentation.ui.composables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.presentation.utilities.validation.Validation
import org.jetbrains.compose.resources.stringResource


@Composable
fun TextFieldWithErrorState(
    modifier: Modifier = Modifier,
    textoState: String,
    textoPista: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    validacionState: Validation,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    onValueChange: (String) -> Unit,
    suffix: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null,
) {
    TextField(
        label = label,
        suffix = suffix,
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
                Text(text = stringResource(validacionState.errorMessage!!))
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
fun OutlinedTextFieldEmail(
    modifier: Modifier = Modifier,
    label: String = "",
    emailState: String,
    validacionState: Validation,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
    iconoInformativo: Painter = rememberVectorPainter(image = Icons.Filled.Email)
) {

    OutlinedTextFieldWithError(
        modifier = modifier,
        textoState = emailState,
        validacionState = validacionState,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(12.dp),
                leadingIcon = iconoInformativo
    )

}


@Composable
fun OutlinedTextFieldWithError(
    modifier: Modifier = Modifier,
    textoState: String,
    textoPista: String = "",
    validacionState: Validation,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    onValueChange: (String) -> Unit,
    suffix: @Composable (() -> Unit)? = null,
    label: String = "",
    shape: Shape = OutlinedTextFieldDefaults.shape,
    leadingIcon: Painter,


    ) {
    OutlinedTextField(
        label = { Text(label) },
        suffix = suffix,
        modifier = modifier,
        value = textoState,
        onValueChange = onValueChange,
        singleLine = true,
        leadingIcon = {
            Icon(
                painter = leadingIcon, contentDescription = label
            )
        },
        placeholder = {
            Text(
                text = textoPista,
                style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
            )
        },
        keyboardOptions = keyboardOptions,
        supportingText = {
            if (validacionState.error) {
                Text(text = stringResource(validacionState.errorMessage!!))
            }
        },
        isError = validacionState.error,
        shape = shape
    )
}

@Composable
fun OutlinedTextFieldWithErrorState(
    modifier: Modifier = Modifier,
    textoState: String,
    textoPista: String = "",
    validacionState: Validation,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    onValueChange: (String) -> Unit,
    suffix: @Composable (() -> Unit)? = null,
    label: String = "",
    shape: Shape = RoundedCornerShape(12.dp)

    ) {
    OutlinedTextField(
        label = { Text(label) },
        suffix = suffix,
        modifier = modifier,
        value = textoState,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = {
            Text(
                text = textoPista,
                style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
            )
        },
        keyboardOptions = keyboardOptions,
        supportingText = {
            if (validacionState.error) {
                Text(text = stringResource(validacionState.errorMessage!!))
            }
        },
        isError = validacionState.error,
        shape = shape
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
                Text(text = stringResource(validacionState.errorMessage!!))
            }
        },
        isError = validacionState.error,
        visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        leadingIcon = {
            Icon(
                painter = iconoInformativo, contentDescription = label
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
    label: String = "Password",
    modifier: Modifier = Modifier,
    passwordState: String,
    validacionState: Validation,
    onValueChange: (String) -> Unit,
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
        supportingText = {
            if (validacionState.error) {
                Text(text = stringResource(validacionState.errorMessage!!))
            }
        },
        isError = validacionState.error,
        visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        leadingIcon = {
            Icon(
                painter = iconoInformativo, contentDescription = label
            )
        },
        trailingIcon = {
            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                val visibilityIcon =
                    if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordHidden) labelShow else labelHide
                Icon(imageVector = visibilityIcon, contentDescription = description)
            }
        },
        shape = RoundedCornerShape(12.dp)
    )
}

