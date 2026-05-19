package com.lazysyntax.nutron.presentation.ui.features.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.himanshoe.charty.color.ChartyColor
import com.himanshoe.charty.line.AreaChart
import com.himanshoe.charty.line.config.LineChartConfig
import com.lazysyntax.nutron.presentation.ui.navigation.composables.NavBar
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadísticas de Progreso") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(StatisticsEvent.OnClickBack) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { NavBar() }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RangeSelector(
                selectedRange = uiState.selectedRange,
                onRangeSelected = { viewModel.onEvent(StatisticsEvent.OnRangeSelected(it)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SummaryCards(
                average = uiState.averageCalories,
                total = uiState.totalCalories
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Consumo de Calorías",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.caloriesData.isNotEmpty()) {
                AreaChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    data = { uiState.caloriesData },
                    color = ChartyColor.Gradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            Color.Transparent
                        )
                    ),
                    lineConfig = LineChartConfig(
                        showPoints = true,
                        smoothCurve = true
                    )
                )
            } else {
                Box(modifier = Modifier.height(300.dp), contentAlignment = Alignment.Center) {
                    Text(
                        "No hay datos para este periodo",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun RangeSelector(
    selectedRange: TimeRange,
    onRangeSelected: (TimeRange) -> Unit
) {
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        TimeRange.entries.forEachIndexed { index, range ->
            val label = when (range) {
                TimeRange.WEEK -> "Semana"
                TimeRange.MONTH -> "Mes"
                TimeRange.THREE_MONTHS -> "3 Meses"
            }
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = TimeRange.entries.size
                ),
                onClick = { onRangeSelected(range) },
                selected = selectedRange == range
            ) {
                Text(label)
            }
        }
    }
}

@Composable
fun SummaryCards(average: Double, total: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.weight(1f)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Promedio Diario", style = MaterialTheme.typography.labelMedium)
                Text("${average.roundToInt()} kcal", style = MaterialTheme.typography.headlineSmall)
            }
        }
        Card(modifier = Modifier.weight(1f)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Total Periodo", style = MaterialTheme.typography.labelMedium)
                Text("${total.roundToInt()} kcal", style = MaterialTheme.typography.headlineSmall)
            }
        }
    }
}
