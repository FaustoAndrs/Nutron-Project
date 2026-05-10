package com.lazysyntax.nutron.main.ui.features.settings

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
import com.lazysyntax.nutron.main.ui.navigation.composables.NavBar
import com.lazysyntax.nutron.main.ui.navigation.composables.TopAppBarCommon
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.settings_button_language
import nutron.composeapp.generated.resources.settings_button_logout
import nutron.composeapp.generated.resources.settings_button_setup
import nutron.composeapp.generated.resources.settings_headline
import nutron.composeapp.generated.resources.title_settings
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    onLogOut: () -> Unit,
    onSetUp: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBarCommon(stringResource(Res.string.title_settings)) },
        bottomBar = { NavBar() }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.settings_headline),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.logout { onLogOut() } },
                content = { Text(stringResource(Res.string.settings_button_logout)) }
            )
            Button(
                onClick = { onSetUp() },
                content = { Text(stringResource(Res.string.settings_button_setup)) }
            )
            Button(
                onClick = { viewModel.onSettingsEvent(SettingsEvent.OnClickLanguage) },
                content = { 
                    Text(
                        text = stringResource(Res.string.settings_button_language) + " : ${uiState.language}"
                    )
                }
            )
        }
    }
}
