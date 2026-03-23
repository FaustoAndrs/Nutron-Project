@file:OptIn(ExperimentalMaterial3Api::class)

package com.lazysyntax.nutron

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import androidx.savedstate.serialization.SavedStateConfiguration
import com.lazysyntax.nutron.main.ui.composables.TextFieldEmail
import com.lazysyntax.nutron.main.ui.composables.TextFieldPassword
import com.lazysyntax.nutron.main.ui.features.login.LoginScreen
import kotlinx.serialization.modules.subclass
import nutron.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource


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

private val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Route::class)

        }
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
                backStack.clear()
                backStack.add(route)
            }
        }

        NavDisplay(
            backStack = backStack,
            onBack = {
                if (backStack.size > 1) {
                    backStack.removeAt(backStack.size - 1)
                }
            },
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
                        onNavigateToProfile = { backStack.clear(); backStack.add(Route.Profile) },
                        onNavigateToSignUp = { backStack.add(Route.SignUp) },
                        onNavigateToSkip = { backStack.add(Route.SetUp(fromSignUp = false)) }
                    )

                    Route.SignUp -> SignUpScreen(
                        onBack = { backStack.removeAt(backStack.size - 1) },
                        onSignUpSuccess = {
                            backStack.add(Route.SetUp(fromSignUp = true))
                        }
                    )

                    is Route.SetUp -> SetupScreen(
                        fromSignUp = route.fromSignUp,
                        onBack = {
                            if (route.fromSignUp) {
                                backStack.removeAt(backStack.size - 1)
                            } else {
                                backStack.clear()
                                backStack.add(Route.Login)
                            }
                        },
                        onFinish = {
                            backStack.clear()
                            backStack.add(Route.Profile)
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
                            backStack.clear()
                            backStack.add(Route.Login)
                        },
                        onSetUp = {
                            backStack.add(Route.SetUp(fromSignUp = false))
                        }
                    )

                    is Route.Details -> DetailsScreen(
                        id = route.id,
                        onBack = { backStack.removeAt(backStack.size - 1) }
                    )
                }
            }
        }
    }
}

@Composable
fun SignUpScreen(onBack: () -> Unit, onSignUpSuccess: () -> Unit) {
    Scaffold(
        topBar = { TopAppBarWhitBackButtonCommon(stringResource(Res.string.title_signup), onBack) },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(Res.string.signup_headline),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onSignUpSuccess) {
                Text(stringResource(Res.string.signup_button_continue))
            }
        }
    }
}

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

@Composable
fun ProfileScreen(onNavigateToDetails: (String) -> Unit, onNavigateToScreen: (Int) -> Unit) {
    Scaffold(
        topBar = { TopAppBarCommon(stringResource(Res.string.title_profile)) },
        bottomBar = { NavBar(0, onNavigateToScreen = onNavigateToScreen) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(Res.string.profile_headline),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onNavigateToDetails("user_1") }) {
                Text(stringResource(Res.string.profile_button_details))
            }
        }
    }
}

@Composable
fun TargetsScreen(onNavigateToScreen: (Int) -> Unit) {
    Scaffold(
        topBar = { TopAppBarCommon(stringResource(Res.string.title_targets)) },
        bottomBar = { NavBar(1, onNavigateToScreen = onNavigateToScreen) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(Res.string.targets_headline),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
fun DiaryScreen(onNavigateToScreen: (Int) -> Unit) {
    Scaffold(
        topBar = { TopAppBarCommon(stringResource(Res.string.title_diary)) },
        bottomBar = { NavBar(2, onNavigateToScreen = onNavigateToScreen) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(Res.string.diary_headline),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

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

@Composable
fun DetailsScreen(id: String, onBack: () -> Unit) {
    Scaffold(topBar = { TopAppBarCommon(stringResource(Res.string.title_details)) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(Res.string.details_id, id))
            Button(onClick = onBack) { Text(stringResource(Res.string.button_back)) }
        }
    }
}

@Composable
fun NavBar(indexScreenState: Int, onNavigateToScreen: (Int) -> Unit) {
    val items = remember {
        listOf(
            Res.string.nav_profile to Icons.Default.Person,
            Res.string.nav_targets to Icons.Default.Info,
            Res.string.nav_diary to Icons.Default.Star,
            Res.string.nav_settings to Icons.Default.Settings,
        )
    }
    NavigationBar {
        items.forEachIndexed { index, (resId, icon) ->
            val title = stringResource(resId)
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = title) },
                label = { Text(title) },
                selected = indexScreenState == index,
                onClick = { onNavigateToScreen(index) }
            )
        }
    }
}

@Composable
fun TopAppBarCommon(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
) =
    CenterAlignedTopAppBar(
        title = { Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        scrollBehavior = scrollBehavior
    )

@Composable
fun TopAppBarWhitBackButtonCommon(
    title: String,
    onBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
) =
    CenterAlignedTopAppBar(
        title = { Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.button_back)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )