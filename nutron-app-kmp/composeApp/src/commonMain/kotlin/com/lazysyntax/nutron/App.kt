@file:OptIn(ExperimentalMaterial3Api::class)

package com.lazysyntax.nutron

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import com.lazysyntax.nutron.main.ui.navigation.NavDisplayNutron
import com.lazysyntax.nutron.main.ui.navigation.Route
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

private val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {}
    }
}

@Composable
@Preview
fun App() {
    MaterialTheme {

        val backStack = rememberNavBackStack(navConfig, Route.Login as NavKey)

        val navigateToTab: (Int) -> Unit = { index ->
            val route = when (index) {
                0 -> Route.Profile
                1 -> Route.Targets
                2 -> Route.Diary
                3 -> Route.Settings
                else -> Route.Profile
            }
            if (backStack.lastOrNull() != route) {
                backStack.add(route)
                while (backStack.size > 1) {
                    backStack.removeAt(0)
                }
            }
        }

        NavDisplayNutron(backStack, navigateToTab)
    }
}