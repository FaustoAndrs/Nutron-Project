package com.lazysyntax.nutron.main.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.lazysyntax.nutron.main.ui.features.diary.DetailsScreen
import com.lazysyntax.nutron.main.ui.features.diary.DiaryScreen
import com.lazysyntax.nutron.main.ui.features.login.LoginScreen
import com.lazysyntax.nutron.main.ui.features.login.setUp.SetupScreen
import com.lazysyntax.nutron.main.ui.features.login.signUp.SignUpScreen
import com.lazysyntax.nutron.main.ui.features.profile.ProfileScreen
import com.lazysyntax.nutron.main.ui.features.settings.SettingsScreen
import com.lazysyntax.nutron.main.ui.features.targets.TargetsScreen

@Composable
fun NavDisplayNutron(backStack: NavBackStack<NavKey>, navigateToTab: (Int) -> Unit) {

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        transitionSpec = {
            fadeIn(animationSpec = tween(200)) togetherWith fadeOut(animationSpec = tween(200))
        },
        popTransitionSpec = {
            fadeIn(animationSpec = tween(200)) togetherWith fadeOut(animationSpec = tween(200))
        }
    ) { route ->
        NavEntry(route) {
            when (route) {
                Route.Login -> LoginScreen(
                    onNavigateToProfile = {
                        backStack.add(Route.Profile)
                        while (backStack.size > 1) {
                            backStack.removeAt(0)
                        }
                    },
                    onNavigateToSignUp = { backStack.add(Route.SignUp) },
                    onNavigateToSkip = { backStack.add(Route.SetUp(fromSignUp = false)) }
                )

                Route.SignUp -> SignUpScreen(
                    onBack = {
                        if (backStack.size > 1) {
                            backStack.removeAt(backStack.size - 1)
                        }
                    },
                    onSignUpSuccess = {
                        backStack.add(Route.SetUp(fromSignUp = true))
                    }
                )

                is Route.SetUp -> SetupScreen(
                    fromSignUp = route.fromSignUp,
                    onBack = {
                        if (route.fromSignUp) {
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.size - 1)
                            }
                        } else {
                            backStack.add(Route.Login)
                            while (backStack.size > 1) {
                                backStack.removeAt(0)
                            }
                        }
                    },
                    onFinish = {
                        backStack.add(Route.Profile)
                        while (backStack.size > 1) {
                            backStack.removeAt(0)
                        }
                    }
                )

                Route.Profile -> ProfileScreen(
                    onNavigateToDetails = { id -> backStack.add(Route.Details(id)) },
                    onNavigateToScreen = navigateToTab
                )

                Route.Targets -> TargetsScreen(
                    onNavigateToScreen = navigateToTab
                )

                Route.Diary -> DiaryScreen(
                    onNavigateToScreen = navigateToTab
                )

                Route.Settings -> SettingsScreen(
                    onNavigateToScreen = navigateToTab,
                    onLogOut = {
                        backStack.add(Route.Login)
                        while (backStack.size > 1) {
                            backStack.removeAt(0)
                        }
                    },
                    onSetUp = {
                        backStack.add(Route.SetUp(fromSignUp = false))
                    }
                )

                is Route.Details -> DetailsScreen(
                    id = route.id,
                    onBack = {
                        if (backStack.size > 1) {
                            backStack.removeAt(backStack.size - 1)
                        }
                    }
                )
            }
        }
    }
}
