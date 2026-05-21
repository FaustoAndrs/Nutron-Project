@file:OptIn(ExperimentalMaterial3Api::class)

package com.lazysyntax.nutron.presentation.ui.navigation.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.presentation.ui.navigation.Navigator
import com.lazysyntax.nutron.presentation.ui.navigation.Route
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.button_back
import nutron.composeapp.generated.resources.diary
import nutron.composeapp.generated.resources.nav_diary
import nutron.composeapp.generated.resources.nav_profile
import nutron.composeapp.generated.resources.nav_settings
import nutron.composeapp.generated.resources.nav_targets
import nutron.composeapp.generated.resources.profile
import nutron.composeapp.generated.resources.settings
import nutron.composeapp.generated.resources.targets
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject

@OptIn(InternalResourceApi::class)
@Composable
fun NavBar() { // Sin parámetros
    val navigator: Navigator = koinInject()
    val currentRoute = navigator.backstack.lastOrNull()

    val items = remember {
        listOf(
            Triple(Route.Profile, Res.string.nav_profile, Res.drawable.profile),
            Triple(Route.Targets, Res.string.nav_targets, Res.drawable.targets),
            Triple(Route.Diary, Res.string.nav_diary, Res.drawable.diary),
            Triple(Route.Settings, Res.string.nav_settings, Res.drawable.settings)
        )
    }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        items.forEach { (route, resId, icon) ->
            val isSelected = currentRoute == route
            NavigationBarItem(
                icon = { Icon(vectorResource(icon), contentDescription = null) },
                label = { Text(stringResource(resId)) },
                selected = isSelected,
                onClick = {
                    if (!isSelected) navigator.resetTo(route)
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = MaterialTheme.colorScheme.tertiary,
                    unselectedTextColor = MaterialTheme.colorScheme.tertiary,
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0f)
                )
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
