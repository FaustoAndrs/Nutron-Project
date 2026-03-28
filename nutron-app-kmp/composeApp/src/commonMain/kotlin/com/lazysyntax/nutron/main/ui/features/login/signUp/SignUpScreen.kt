package com.lazysyntax.nutron.main.ui.features.login.signUp

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.main.ui.navigation.TopAppBarWhitBackButtonCommon
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.signup_button_continue
import nutron.composeapp.generated.resources.signup_headline
import nutron.composeapp.generated.resources.title_signup
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(onBack: () -> Unit, onSignUpSuccess: () -> Unit) {
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
            Button(onClick = onSignUpSuccess) {
                Text(stringResource(Res.string.signup_button_continue))
            }
        }
    }
}