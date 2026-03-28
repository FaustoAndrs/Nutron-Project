package com.lazysyntax.nutron.main.ui.features.diary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lazysyntax.nutron.main.ui.navigation.TopAppBarCommon
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.button_back
import nutron.composeapp.generated.resources.details_id
import nutron.composeapp.generated.resources.title_details
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
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
