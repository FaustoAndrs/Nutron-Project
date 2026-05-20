package com.lazysyntax.nutron.presentation.ui.features.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.presentation.ui.composables.OutlinedTextFieldEmail
import com.lazysyntax.nutron.presentation.ui.composables.OutlinedTextFieldPassword
import com.lazysyntax.nutron.presentation.ui.composables.TextFieldEmail
import com.lazysyntax.nutron.presentation.ui.composables.TextFieldPassword
import com.lazysyntax.nutron.presentation.ui.navigation.composables.TopAppBarCommon
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.login_button_enter
import nutron.composeapp.generated.resources.login_button_signup
import nutron.composeapp.generated.resources.login_button_skip
import nutron.composeapp.generated.resources.login_headline
import nutron.composeapp.generated.resources.login_signup_text
import nutron.composeapp.generated.resources.title_welcome
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    vm: LoginViewModel = koinViewModel(),
) {
    val uiState by vm.uiState.collectAsState()
    val validationState by vm.validationState.collectAsState()

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess == true) {
            vm.onLoginEvent(LoginEvent.OnLoginSuccess)
            vm.resetLoginState()
        }
    }
    LoginContent(
        uiState = uiState,
        emailChanged = { vm.onLoginEvent(LoginEvent.EmailChanged(it)) },
        passwordChanged = { vm.onLoginEvent(LoginEvent.PasswordChanged(it)) },
        onClickLogin = { vm.onLoginEvent(LoginEvent.OnClickLogin) },
        onClickSignUp = { vm.onLoginEvent(LoginEvent.OnClickSignUp) },
        onClickSkipLogin = { vm.onLoginEvent(LoginEvent.OnClickSkipLogin) },
        validationState = validationState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    uiState: LoginUiState,
    emailChanged: (String) -> Unit,
    passwordChanged: (String) -> Unit,
    onClickLogin: () -> Unit,
    onClickSignUp: () -> Unit,
    onClickSkipLogin: () -> Unit,
    validationState: LoginUiStateValidation,
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = { TopAppBarCommon("") },
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(Res.string.login_headline),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Introduce tus credenciales para continuar",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
                OutlinedTextFieldEmail(
                    label = "Email",
                    emailState = uiState.email,
                    validacionState = validationState.emailValidation,
                    onValueChange = emailChanged,
                    modifier = Modifier.fillMaxWidth()
                )
            OutlinedTextFieldPassword(
                passwordState = uiState.password,
                validacionState = validationState.passwordValidation,
                onValueChange = passwordChanged,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = onClickLogin,
                    enabled = !validationState.error,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        stringResource(Res.string.login_button_enter),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            uiState.errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(stringResource(Res.string.login_signup_text), style = MaterialTheme.typography.bodyMedium)
                Text(
                    stringResource(Res.string.login_button_signup),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable(onClick = onClickSignUp).padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                stringResource(Res.string.login_button_skip),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.clickable(onClick = onClickSkipLogin).padding(8.dp)
            )
        }
    }
}
