package com.lazysyntax.nutron.main.ui.features.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.main.ui.composables.TextFieldEmail
import com.lazysyntax.nutron.main.ui.composables.TextFieldPassword
import com.lazysyntax.nutron.main.ui.navigation.TopAppBarCommon
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.login_button_enter
import nutron.composeapp.generated.resources.login_button_signup
import nutron.composeapp.generated.resources.login_button_skip
import nutron.composeapp.generated.resources.login_headline
import nutron.composeapp.generated.resources.login_signup_text
import nutron.composeapp.generated.resources.title_welcome
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToSkip: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val validationState by viewModel.validationState.collectAsState()

    Scaffold(
        topBar = { TopAppBarCommon(stringResource(Res.string.title_welcome)) },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.login_headline),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextFieldEmail(
                label = "Email",
                emailState = uiState.email,
                validacionState = validationState.emailValidation,
                onValueChange = { viewModel.onEmailChanged(it) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextFieldPassword(
                label = "Contraseña",
                passwordState = uiState.password,
                validacionState = validationState.passwordValidation,
                onValueChange = { viewModel.onPasswordChanged(it) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onNavigateToProfile,
                enabled = !validationState.error
            ) {
                Text(stringResource(Res.string.login_button_enter))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(stringResource(Res.string.login_signup_text))
            Text(
                stringResource(Res.string.login_button_signup),
                style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.primary),
                modifier = Modifier.clickable(onClick = onNavigateToSignUp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                stringResource(Res.string.login_button_skip),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.clickable(onClick = onNavigateToSkip)
            )
        }
    }
}
