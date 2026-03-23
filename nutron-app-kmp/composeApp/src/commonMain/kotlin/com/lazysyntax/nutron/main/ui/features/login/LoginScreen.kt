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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.TopAppBarCommon
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.login_button_enter
import nutron.composeapp.generated.resources.login_button_signup
import nutron.composeapp.generated.resources.login_button_skip
import nutron.composeapp.generated.resources.login_headline
import nutron.composeapp.generated.resources.login_signup_text
import nutron.composeapp.generated.resources.title_welcome
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    onNavigateToSkip: () -> Unit
) {
    var loginState by remember { mutableStateOf("") }

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

            /*
                TextFieldEmail(
                    label = "Login",
                    emailState = TODO(),
                    validacionState = TODO(),
                    onValueChange = TODO()
                )

                TextFieldPassword(
                    label = "Password",
                    passwordState = TODO(),
                    validacionState = TODO(),
                    onValueChange = TODO()
                )
    */

            TextField(
                label = {Text("Login")},
                value = loginState,
                onValueChange = {loginState = it})

            TextField(
                label = {Text("password")},
                value = loginState,
                onValueChange = {loginState = it})



            Button(onClick = onNavigateToProfile) {
                Text(stringResource(Res.string.login_button_enter))
            }

            Text(stringResource(Res.string.login_signup_text))
            Text(
                stringResource(Res.string.login_button_signup),
                modifier = Modifier.clickable(onClick = onNavigateToSignUp))


            Text(stringResource(Res.string.login_button_skip),
                modifier = Modifier.clickable(onClick = onNavigateToSkip))

        }
    }
}
