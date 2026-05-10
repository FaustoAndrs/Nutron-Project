package com.lazysyntax.nutron.main.ui.features.login.signUp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.main.ui.composables.TextFieldEmail
import com.lazysyntax.nutron.main.ui.composables.TextFieldPassword
import com.lazysyntax.nutron.main.ui.composables.TextFieldWithErrorState
import com.lazysyntax.nutron.main.ui.navigation.composables.TopAppBarWhitBackButtonCommon
import com.lazysyntax.nutron.models.NewUser
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.signup_button_continue
import nutron.composeapp.generated.resources.signup_headline
import nutron.composeapp.generated.resources.title_signup
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

@OptIn(ExperimentalMaterial3Api::class)
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
    Scaffold(
        topBar = { TopAppBarWhitBackButtonCommon(stringResource(Res.string.title_signup), onBack) },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(Res.string.signup_headline),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextFieldWithErrorState(
                textoPista = "User name",
                textoState = uiState.userName,
                validacionState = validationState.userNameValidation,
                onValueChange = userNameChanged
            )
            TextFieldWithErrorState(
                textoPista = "Full name",
                textoState = uiState.fullName,
                validacionState = validationState.fullNameValidation,
                onValueChange = fullNameChanged
            )

            TextFieldEmail(
                label = "Email",
                emailState = uiState.email,
                validacionState = validationState.emailValidation,
                onValueChange = emailChangeded
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextFieldPassword(
                label = "Contraseña",
                passwordState = uiState.password,
                validacionState = validationState.passwordValidation,
                onValueChange = passwordChanged
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                val newUser = NewUser(
                    userName = uiState.userName,
                    fullName = uiState.fullName,
                    email = uiState.email,
                    password = uiState.password
                )
                Button(
                    onClick = onClickSignUp,
                    enabled = !validationState.error
                ) {
                    Text(stringResource(Res.string.signup_button_continue))
                }
            }

            uiState.errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = error, color = Color.Red)
            }

        }
    }
}