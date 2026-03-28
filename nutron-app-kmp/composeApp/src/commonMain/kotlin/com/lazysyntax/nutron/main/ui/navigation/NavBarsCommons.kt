@file:OptIn(ExperimentalMaterial3Api::class)

package com.lazysyntax.nutron.main.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextOverflow
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.button_back
import nutron.composeapp.generated.resources.nav_diary
import nutron.composeapp.generated.resources.nav_profile
import nutron.composeapp.generated.resources.nav_settings
import nutron.composeapp.generated.resources.nav_targets
import org.jetbrains.compose.resources.stringResource

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
