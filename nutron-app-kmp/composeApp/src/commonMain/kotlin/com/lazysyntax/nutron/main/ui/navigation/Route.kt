package com.lazysyntax.nutron.main.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.lazysyntax.nutron.models.NewUser
import kotlinx.serialization.Serializable

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
    data object Library : Route

    @Serializable
    data object Settings : Route

    @Serializable
    data class Details(val id: String) : Route
}