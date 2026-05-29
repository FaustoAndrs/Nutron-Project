package com.lazysyntax.nutron.presentation.ui.features.diary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.presentation.theme.CaloriesIndexColor
import com.lazysyntax.nutron.presentation.theme.CarbohydratesIndexColor
import com.lazysyntax.nutron.presentation.theme.FatsIndexColor
import com.lazysyntax.nutron.presentation.theme.ProteinsIndexColor
import com.lazysyntax.nutron.presentation.ui.features.diary.composables.DiaryTopAppBar
import com.lazysyntax.nutron.presentation.ui.features.diary.composables.MacroProgressBar
import com.lazysyntax.nutron.presentation.ui.features.diary.composables.MacrosCount
import com.lazysyntax.nutron.presentation.ui.features.diary.composables.MealCard
import com.lazysyntax.nutron.presentation.ui.features.diary.composables.calculateMacros
import com.lazysyntax.nutron.presentation.ui.features.diary.composables.calculateTargetGrams

import com.lazysyntax.nutron.presentation.ui.navigation.composables.BottomNavBar
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.label_calories
import nutron.composeapp.generated.resources.label_carbs_short
import nutron.composeapp.generated.resources.label_fats
import nutron.composeapp.generated.resources.label_proteins_short
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
    val listState = rememberLazyListState()
    val showLabels by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset < 50
        }
    }

    Scaffold(
        topBar = {
            DiaryTopAppBar(
                uiState = uiState,
                onEvent = onDiaryEvent
            )
        },
        bottomBar = { BottomNavBar() },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val totalMacros = uiState.meals?.fold(MacrosCount()) { acc, meal ->
                acc + meal.calculateMacros()
            } ?: MacrosCount()

            val targetMacros = uiState.targets.calculateTargetGrams()

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor =  MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            )
            {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MacroProgressBar(
                        label = stringResource(Res.string.label_calories),
                        current = totalMacros.calories,
                        target = targetMacros.calories,
                        unit = "kcal",
                        color = CaloriesIndexColor,
                        showLabels = showLabels
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MacroProgressBar(
                            label = stringResource(Res.string.label_proteins_short),
                            current = totalMacros.proteins,
                            target = targetMacros.proteins,
                            unit = "g",
                            color = ProteinsIndexColor,
                            modifier = Modifier.weight(1f),
                            showLabels = showLabels
                        )
                        MacroProgressBar(
                            label = stringResource(Res.string.label_carbs_short),
                            current = totalMacros.carbohydrates,
                            target = targetMacros.carbohydrates,
                            unit = "g",
                            color = CarbohydratesIndexColor,
                            modifier = Modifier.weight(1f),
                            showLabels = showLabels
                        )
                        MacroProgressBar(
                            label = stringResource(Res.string.label_fats),
                            current = totalMacros.fats,
                            target = targetMacros.fats,
                            unit = "g",
                            color = FatsIndexColor,
                            modifier = Modifier.weight(1f),
                            showLabels = showLabels
                        )
                    }
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
            ) {
                uiState.meals?.let { meals ->
                    items(meals) { meal ->
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            MealCard(
                                meal = meal,
                                onAddProduct = { onDiaryEvent(DiaryEvent.OnClickAddProduct(meal)) },
                                onDeleteFood = { food ->
                                    onDiaryEvent(
                                        DiaryEvent.OnDeleteFood(meal, food)
                                    )
                                }
                            )
                        }
                    }
                }
            }

        }
    }
}
