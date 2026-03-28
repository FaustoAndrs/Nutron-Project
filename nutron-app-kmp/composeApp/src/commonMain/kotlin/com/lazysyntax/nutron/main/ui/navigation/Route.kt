package com.lazysyntax.nutron.main.ui.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object Login : Route

    @Serializable
    data object SignUp : Route

    @Serializable
    data class SetUp(val fromSignUp: Boolean) : Route

    @Serializable
    data object Profile : Route

    @Serializable
    data object Targets : Route

    @Serializable
    data object Diary : Route

    @Serializable
    data object Settings : Route

    @Serializable
    data class Details(val id: String) : Route
}