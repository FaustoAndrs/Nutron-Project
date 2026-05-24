package com.lazysyntax.nutron.presentation.ui.features.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.presentation.ui.navigation.composables.NavBar
import com.lazysyntax.nutron.presentation.ui.navigation.composables.TopAppBarCommon
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.barcode_scanner_24px
import nutron.composeapp.generated.resources.create_account
import nutron.composeapp.generated.resources.diary
import nutron.composeapp.generated.resources.settings_button_language
import nutron.composeapp.generated.resources.settings_button_logout
import nutron.composeapp.generated.resources.settings_button_setup
import nutron.composeapp.generated.resources.settings_button_signup
import nutron.composeapp.generated.resources.settings_dark_theme
import nutron.composeapp.generated.resources.settings_diary_settings
import nutron.composeapp.generated.resources.settings_generate_test_data
import nutron.composeapp.generated.resources.settings_headline
import nutron.composeapp.generated.resources.settings_logout_confirm_message
import nutron.composeapp.generated.resources.settings_logout_confirm_title
import nutron.composeapp.generated.resources.settings_on_confirm
import nutron.composeapp.generated.resources.settings_on_dimiss
import nutron.composeapp.generated.resources.settings_section_app
import nutron.composeapp.generated.resources.settings_section_sync
import nutron.composeapp.generated.resources.settings_sync_advise_message
import nutron.composeapp.generated.resources.settings_sync_advise_title
import nutron.composeapp.generated.resources.settings_sync_pending_headline
import nutron.composeapp.generated.resources.settings_sync_pending_supporting
import nutron.composeapp.generated.resources.title_settings
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    onSetUp: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBarCommon(stringResource(Res.string.title_settings)) },
        bottomBar = { NavBar() },
        containerColor = MaterialTheme.colorScheme.background // Fondo neutro para resaltar las tarjetas
    ) { padding ->
        if (uiState.showLogoutConfirmation) {
            AlertDialog(
                onDismissRequest = { viewModel.onSettingsEvent(SettingsEvent.OnDismissLogOut) },
                title = { Text(stringResource(Res.string.settings_logout_confirm_title)) },
                text = {

                    Text(stringResource(Res.string.settings_logout_confirm_message))

                       },
                confirmButton = {
                    TextButton(
                        onClick = { viewModel.onSettingsEvent(SettingsEvent.OnDismissLogOut) }
                    ) {
                        Text(stringResource(Res.string.settings_on_dimiss))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { viewModel.onSettingsEvent(SettingsEvent.OnConfirmLogOut) }
                    ) {
                        Text(stringResource(Res.string.settings_on_confirm), color = MaterialTheme.colorScheme.error)
                    }

                }
            )
        }
        if (uiState.showAdviseCreateAccount) {
            AlertDialog(
                onDismissRequest = { viewModel.onSettingsEvent(SettingsEvent.OnConfirmAdviseCreateAccount) },
                title = { Text(stringResource(Res.string.settings_sync_advise_title)) },
                text = {

                    Text(stringResource(Res.string.settings_sync_advise_message))

                },
                confirmButton = {
                    TextButton(
                        onClick = { viewModel.onSettingsEvent(SettingsEvent.OnConfirmAdviseCreateAccount) }
                    ) {
                        Text(stringResource(Res.string.settings_on_dimiss), color = MaterialTheme.colorScheme.primary)
                    }
                },

            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            if (uiState.unsyncedMealsCount > 0 && uiState.showMealsCountAdvise) {
                item {
                    SettingsSection(title = stringResource(Res.string.settings_section_sync)) {
                        ListItem(
                            headlineContent = {
                                Text(stringResource(Res.string.settings_sync_pending_headline))
                            },
                            supportingContent = {
                                Text("${uiState.unsyncedMealsCount}" + stringResource(Res.string.settings_sync_pending_supporting))
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.CloudSync,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            trailingContent = {
                                if (uiState.isSyncing) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.outline
                                    )
                                }
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.clickable(enabled = !uiState.isSyncing) {
                                    viewModel.onSettingsEvent(SettingsEvent.OnClickSync)
                            }
                        )
                    }
                }
            }

            item {
                SettingsSection(title = stringResource(Res.string.settings_section_app)) {
                    ListItem(
                        headlineContent = {
                            Text(
                                text = stringResource(Res.string.settings_diary_settings),
                            )
                        },
                        leadingContent = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.diary),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingContent = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline
                            )
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier.clickable {

                        }
                    )
                    ListItem(
                        headlineContent = { Text(stringResource(Res.string.settings_dark_theme)) },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Default.DarkMode,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingContent = {
                            Switch(
                                checked = uiState.isDarkTheme,
                                onCheckedChange = { enabled ->
                                    viewModel.onSettingsEvent(SettingsEvent.OnToggleDarkTheme(enabled))
                                }
                            )
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
            }
            item {
                SettingsSection(title = stringResource(Res.string.settings_headline)) {
                    ListItem(
                        headlineContent = { Text(stringResource(Res.string.settings_button_language)) },
                        supportingContent = { Text(uiState.language.uppercase()) },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingContent = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline
                            )
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier.clickable {
                            viewModel.onSettingsEvent(SettingsEvent.OnClickLanguage)
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                    ListItem(
                        headlineContent = { Text(stringResource(Res.string.settings_button_setup)) },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingContent = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline
                            )
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier.clickable { onSetUp() }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                    ListItem(
                        headlineContent = { Text(stringResource(Res.string.settings_generate_test_data)) },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Default.Science,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        },
                        /*trailingContent = {
                            if (uiState.isGeneratingTestData) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        },*/
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier.clickable(enabled = !uiState.isGeneratingTestData) {
                            viewModel.onSettingsEvent(SettingsEvent.GenerateTestData)
                        }
                    )
                }
            }
            item {
                SettingsSection {

                    if (uiState.isGuestLogged) {
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = stringResource(Res.string.settings_button_signup),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            },
                            leadingContent = {
                                Icon(
                                    imageVector = vectorResource(Res.drawable.create_account),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            },
                            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.clickable {
                                viewModel.onSettingsEvent(SettingsEvent.OnCreateAccount)
                            }
                        )
                    }
                }
            }
                //Logout
            item {
                SettingsSection {
                    ListItem(
                        headlineContent = {
                            Text(
                                text = stringResource(Res.string.settings_button_logout),
                                color = MaterialTheme.colorScheme.error
                            )
                        },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        modifier = Modifier.clickable {
                            viewModel.onSettingsEvent(SettingsEvent.OnClickLogOut)
                        }
                    )
                }
            }
        }
    }

    if (uiState.isGeneratingTestData) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .clickable(enabled = false) {},
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(Res.string.settings_generate_test_data),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        title?.let {
            Text(
                text = it.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
        }
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(content = content)
        }
    }
}

