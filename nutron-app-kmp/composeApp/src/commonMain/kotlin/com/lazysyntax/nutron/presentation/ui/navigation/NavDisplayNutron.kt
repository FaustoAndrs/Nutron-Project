package com.lazysyntax.nutron.presentation.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.lazysyntax.nutron.presentation.ui.features.diary.DiaryScreen
import com.lazysyntax.nutron.presentation.ui.features.diary.library.LibraryScreen
import com.lazysyntax.nutron.presentation.ui.features.diary.macros.MacrosScreen
import com.lazysyntax.nutron.presentation.ui.features.dietPlan.DietScreen
import com.lazysyntax.nutron.presentation.ui.features.login.LoginScreen
import com.lazysyntax.nutron.presentation.ui.features.login.signUp.SignUpScreen
import com.lazysyntax.nutron.presentation.ui.features.profile.ProfileScreen
import com.lazysyntax.nutron.presentation.ui.features.setUp.SetupScreen
import com.lazysyntax.nutron.presentation.ui.features.settings.SettingsScreen
import com.lazysyntax.nutron.presentation.ui.features.statistics.StatisticsScreen
import com.lazysyntax.nutron.presentation.ui.features.targets.TargetsScreen
import com.lazysyntax.nutron.presentation.ui.navigation.Route.Diary
import com.lazysyntax.nutron.presentation.ui.navigation.Route.Library
import com.lazysyntax.nutron.presentation.ui.navigation.Route.Login
import com.lazysyntax.nutron.presentation.ui.navigation.Route.Macros
import com.lazysyntax.nutron.presentation.ui.navigation.Route.Profile
import com.lazysyntax.nutron.presentation.ui.navigation.Route.SetUp
import com.lazysyntax.nutron.presentation.ui.navigation.Route.Settings
import com.lazysyntax.nutron.presentation.ui.navigation.Route.SignUp
import com.lazysyntax.nutron.presentation.ui.navigation.Route.Statistics
import com.lazysyntax.nutron.presentation.ui.navigation.Route.Targets
import org.koin.compose.koinInject

@Composable
fun NavDisplayNutron() {
    val navigator: Navigator = koinInject()

    NavDisplay(backStack = navigator.backstack, transitionSpec = {
        fadeIn(animationSpec = tween(200)) togetherWith fadeOut(animationSpec = tween(200))
    }, popTransitionSpec = {
        fadeIn(animationSpec = tween(200)) togetherWith fadeOut(animationSpec = tween(200))
    }) { route ->
        NavEntry(route) {
            when (route) {
                Login -> LoginScreen()
                SignUp -> SignUpScreen()

                is SetUp -> SetupScreen(fromSignUp = route.fromSignUp)

                Profile -> ProfileScreen()
                Targets -> TargetsScreen()
                Diary -> DiaryScreen()

                is Library -> LibraryScreen()

                is Macros -> MacrosScreen()

                Statistics -> StatisticsScreen()

                Settings -> SettingsScreen(
                    onSetUp = {
                        navigator.navigateTo(
                            SetUp(fromSignUp = false)
                        )
                    }
                )

                Route.DietPlan -> DietScreen()
            }
        }
    }
}
