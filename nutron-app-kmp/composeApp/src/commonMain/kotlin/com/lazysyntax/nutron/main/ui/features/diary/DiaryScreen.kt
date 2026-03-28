package com.lazysyntax.nutron.main.ui.features.diary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lazysyntax.nutron.main.ui.navigation.NavBar
import com.lazysyntax.nutron.main.ui.navigation.TopAppBarCommon
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.diary_headline
import nutron.composeapp.generated.resources.title_diary
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryScreen(onNavigateToScreen: (Int) -> Unit) {
    Scaffold(
        topBar = { TopAppBarCommon(stringResource(Res.string.title_diary)) },
        bottomBar = { NavBar(2, onNavigateToScreen = onNavigateToScreen) }) { padding ->
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