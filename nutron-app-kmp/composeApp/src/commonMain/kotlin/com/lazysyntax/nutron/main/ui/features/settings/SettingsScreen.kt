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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.main.ui.navigation.NavBar
import com.lazysyntax.nutron.main.ui.navigation.TopAppBarCommon
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.settings_button_logout
import nutron.composeapp.generated.resources.settings_button_setup
import nutron.composeapp.generated.resources.settings_headline
import nutron.composeapp.generated.resources.title_settings
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onNavigateToScreen: (Int) -> Unit, onLogOut: () -> Unit, onSetUp: () -> Unit) {
    Scaffold(
        topBar = { TopAppBarCommon(stringResource(Res.string.title_settings)) },
        bottomBar = { NavBar(3, onNavigateToScreen = onNavigateToScreen) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(Res.string.settings_headline),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onLogOut() }) {
                Text(stringResource(Res.string.settings_button_logout))
            }
            Button(onClick = { onSetUp() }) {
                Text(stringResource(Res.string.settings_button_setup))
            }
        }
    }
}
