package com.lazysyntax.nutron.main.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.lazysyntax.nutron.main.ui.features.diary.DetailsScreen
import com.lazysyntax.nutron.main.ui.features.diary.DiaryScreen
import com.lazysyntax.nutron.main.ui.features.diary.DiaryViewModel
import com.lazysyntax.nutron.main.ui.features.diary.library.LibraryScreen
import com.lazysyntax.nutron.main.ui.features.diary.library.LibraryViewModel
import com.lazysyntax.nutron.main.ui.features.login.LoginScreen
import com.lazysyntax.nutron.main.ui.features.login.LoginViewModel
import com.lazysyntax.nutron.main.ui.features.login.signUp.SignUpScreen
import com.lazysyntax.nutron.main.ui.features.login.signUp.SignUpViewModel
import com.lazysyntax.nutron.main.ui.features.profile.ProfileScreen
import com.lazysyntax.nutron.main.ui.features.setUp.SetUpViewModel
import com.lazysyntax.nutron.main.ui.features.setUp.SetupScreen
import com.lazysyntax.nutron.main.ui.features.settings.SettingsScreen
import com.lazysyntax.nutron.main.ui.features.targets.TargetsScreen
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

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
                Route.Login -> LoginScreen()
                Route.SignUp -> SignUpScreen()

                is Route.SetUp -> SetupScreen(fromSignUp = route.fromSignUp)

                Route.Profile -> ProfileScreen()

                Route.Targets -> TargetsScreen()

                Route.Diary -> DiaryScreen()

                is Route.Library -> {
                    val vm: LibraryViewModel = koinViewModel()
                    LibraryScreen(
                        viewModel = vm,
                        onLibraryEvent = vm::onLibraryEvent,
                    )
                }

                Route.Settings -> SettingsScreen(
                    onLogOut = {
                    navigator.resetTo(Route.Login)
                }, onSetUp = {
                    navigator.navigateTo(Route.SetUp(fromSignUp = false))
                })

                is Route.Details -> DetailsScreen(
                    id = route.id, onBack = { navigator.goBack() })
            }
        }
    }
}
