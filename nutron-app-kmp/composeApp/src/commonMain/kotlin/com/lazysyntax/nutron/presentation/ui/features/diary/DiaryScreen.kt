package com.lazysyntax.nutron.presentation.ui.features.diary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.lazysyntax.nutron.presentation.ui.features.diary.composables.DiaryTopAppBar
import com.lazysyntax.nutron.presentation.ui.features.diary.composables.MacroProgressBar
import com.lazysyntax.nutron.presentation.ui.features.diary.composables.MacrosCount
import com.lazysyntax.nutron.presentation.ui.features.diary.composables.MealCard
import com.lazysyntax.nutron.presentation.ui.features.diary.composables.calculateMacros
import com.lazysyntax.nutron.presentation.ui.features.diary.composables.calculateTargetGrams

import com.lazysyntax.nutron.presentation.ui.navigation.composables.NavBar
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
                onEvent = onDiaryEvent
            )
        },
        bottomBar = { NavBar() }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val totalMacros = uiState.meals?.fold(MacrosCount()) { acc, meal ->
                acc + meal.calculateMacros()
            } ?: MacrosCount()

            val targetMacros = uiState.targets.calculateTargetGrams()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Resumen del día",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                MacroProgressBar(
                    label = "Calorías",
                    current = totalMacros.calories,
                    target = targetMacros.calories,
                    unit = "kcal",
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MacroProgressBar(
                        label = "Prot",
                        current = totalMacros.proteins,
                        target = targetMacros.proteins,
                        unit = "g",
                        color = androidx.compose.ui.graphics.Color(0xFFE57373),
                        modifier = Modifier.weight(1f)
                    )
                    MacroProgressBar(
                        label = "Carbs",
                        current = totalMacros.carbohydrates,
                        target = targetMacros.carbohydrates,
                        unit = "g",
                        color = androidx.compose.ui.graphics.Color(0xFFFFB74D),
                        modifier = Modifier.weight(1f)
                    )
                    MacroProgressBar(
                        label = "Grasas",
                        current = totalMacros.fats,
                        target = targetMacros.fats,
                        unit = "g",
                        color = androidx.compose.ui.graphics.Color(0xFF81C784),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            HorizontalDivider()

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {
                uiState.meals?.let { meals ->
                    items(meals) { meal ->
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            MealCard(meal = meal, onAddProduct = {
                                onDiaryEvent(DiaryEvent.OnClickAddProduct(
                                meal
                            )) })
                        }
                    }
                }
            }

        }
    }
}
