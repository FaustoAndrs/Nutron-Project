package com.lazysyntax.nutron.main.ui.features.login.setUp

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
import nutron.composeapp.generated.resources.setup_button_finish
import nutron.composeapp.generated.resources.setup_desc_guest
import nutron.composeapp.generated.resources.setup_desc_user
import nutron.composeapp.generated.resources.setup_welcome_guest
import nutron.composeapp.generated.resources.setup_welcome_user
import nutron.composeapp.generated.resources.title_setup
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(fromSignUp: Boolean, onBack: () -> Unit, onFinish: () -> Unit) {
    Scaffold(
        topBar = { TopAppBarWhitBackButtonCommon(stringResource(Res.string.title_setup), onBack) },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val title =
                if (fromSignUp) stringResource(Res.string.setup_welcome_user) else stringResource(
                    Res.string.setup_welcome_guest
                )
            val description = if (fromSignUp) {
                stringResource(Res.string.setup_desc_user)
            } else {
                stringResource(Res.string.setup_desc_guest)
            }

            Text(title, style = MaterialTheme.typography.headlineMedium)
            Text(description, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onFinish) {
                Text(stringResource(Res.string.setup_button_finish))
            }
        }
    }
}
