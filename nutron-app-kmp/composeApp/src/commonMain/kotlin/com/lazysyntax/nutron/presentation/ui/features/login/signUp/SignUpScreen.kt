package com.lazysyntax.nutron.presentation.ui.features.login.signUp

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.presentation.ui.composables.OutlinedTextFieldEmail
import com.lazysyntax.nutron.presentation.ui.composables.OutlinedTextFieldPassword
import com.lazysyntax.nutron.presentation.ui.composables.OutlinedTextFieldWithErrorState
import com.lazysyntax.nutron.presentation.ui.navigation.composables.TopAppBarWhitBackButtonCommon
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.signup_button_continue
import nutron.composeapp.generated.resources.signup_email_field
import nutron.composeapp.generated.resources.signup_full_name_field
import nutron.composeapp.generated.resources.signup_headline
import nutron.composeapp.generated.resources.signup_name_field
import nutron.composeapp.generated.resources.signup_passwd_field
import nutron.composeapp.generated.resources.signup_subline
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpScreen(
    vm: SignUpViewModel = koinViewModel()
) {
    val uiState by vm.uiState.collectAsState()
    val validationState by vm.validationState.collectAsState()

    SignUpContent(
        uiState = uiState,
        validationState = validationState,
        userNameChanged = { vm.onSignUpEvent(SignUpEvent.UserNameChanged(it)) },
        fullNameChanged = { vm.onSignUpEvent(SignUpEvent.FullNameChanged(it)) },
        emailChangeded = { vm.onSignUpEvent(SignUpEvent.EmailChanged(it)) },
        passwordChanged = { vm.onSignUpEvent(SignUpEvent.PasswordChanged(it)) },
        onClickSignUp = { vm.onSignUpEvent(SignUpEvent.OnClickSignUp) },
        onBack = { vm.onSignUpEvent(SignUpEvent.OnBack) },
    )
}

@OptIn(ExperimentalMaterial3Api::class, InternalResourceApi::class)
@Composable
fun SignUpContent(
    onBack: () -> Unit,
    uiState: SignUpUiState,
    userNameChanged: (String) -> Unit,
    fullNameChanged: (String) -> Unit,
    emailChangeded: (String) -> Unit,
    passwordChanged: (String) -> Unit,
    onClickSignUp: () -> Unit,
    validationState: SignUpUiStateValidation,
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    Scaffold(
        topBar = { TopAppBarWhitBackButtonCommon("", onBack) },
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(Res.string.signup_headline),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(Res.string.signup_subline),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Spacer(modifier = Modifier.height(32.dp))



            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(Res.string.signup_name_field),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedTextFieldWithErrorState(
                    modifier = Modifier.fillMaxWidth(),
                    textoState = uiState.userName,
                    textoPista = "John",
                    validacionState = validationState.userNameValidation,
                    onValueChange = userNameChanged,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(Res.string.signup_full_name_field),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedTextFieldWithErrorState(
                    modifier = Modifier.fillMaxWidth(),
                    textoState = uiState.fullName,
                    textoPista = "John Doe",
                    validacionState = validationState.fullNameValidation,
                    onValueChange = fullNameChanged,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(Res.string.signup_email_field),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                OutlinedTextFieldEmail(
                    label = "Email",
                    emailState = uiState.email,
                    validacionState = validationState.emailValidation,
                    onValueChange = emailChangeded,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(Res.string.signup_passwd_field),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedTextFieldPassword(
                    label = "Contraseña",
                    passwordState = uiState.password,
                    validacionState = validationState.passwordValidation,
                    onValueChange = passwordChanged,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = onClickSignUp,
                    enabled = !validationState.error,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        stringResource(Res.string.signup_button_continue),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            uiState.errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
