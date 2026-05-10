package com.lazysyntax.nutron.main.ui.features.diary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.main.ui.features.diary.composables.DiaryTopAppBar
import com.lazysyntax.nutron.main.ui.features.diary.composables.MealCard

import com.lazysyntax.nutron.main.ui.navigation.composables.NavBar
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.diary_headline
import nutron.composeapp.generated.resources.title_diary
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun DiaryScreen(
    viewModel: DiaryViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    DiaryContent(
        uiState = uiState,
        onDiaryEvent = viewModel::onDiaryEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryContent(
    uiState: DiaryUiState,
    onDiaryEvent: (DiaryEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            DiaryTopAppBar(
                uiState = uiState,
                title = stringResource(Res.string.title_diary),
                onEvent = onDiaryEvent
            )
        },
        bottomBar = { NavBar() }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            //verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    //.fillMaxSize()
                    .weight(1f) // weight(1f) para que ocupe el espacio sobrante
            ) {
                uiState.meals?.let { meals ->
                    items(meals) { meal ->
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp)
                        ) {
                            MealCard(meal = meal, onAddProduct = { onDiaryEvent(DiaryEvent.OnClickAddProduct) })
                            HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                }
            }
            Text(
                stringResource(Res.string.diary_headline),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
