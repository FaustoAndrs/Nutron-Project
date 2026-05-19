@file:OptIn(ExperimentalMaterial3Api::class)

package com.lazysyntax.nutron

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.lazysyntax.nutron.data.remote.authentication.SessionManager
import com.lazysyntax.nutron.presentation.ui.navigation.NavDisplayNutron
import com.lazysyntax.nutron.presentation.utilities.language.changeLanguage
import com.lazysyntax.nutron.presentation.theme.Theme
import org.koin.compose.koinInject
import kotlin.time.Clock


@Composable
@Preview
fun App() {
    val sessionManager: SessionManager = koinInject()
    // Observamos un estado que cambie al hacer logout
    val authSession by sessionManager.authSession.collectAsState()

    val language by sessionManager.language.collectAsState()
    val isDarkTheme by sessionManager.isDarkTheme.collectAsState()

    // Usamos el ID del usuario como KEY. Si cambia a null, todo se reinicia.
    val sessionKey = remember(authSession) {
        authSession?.userId ?: "guest_${Clock.System.now().toEpochMilliseconds()}"
    }

    LaunchedEffect(language) {
        changeLanguage(language)
    }

    Theme(darkTheme = isDarkTheme) {
        key(sessionKey) { // Importa androidx.compose.runtime.key
            NavDisplayNutron()
        }
    }
}