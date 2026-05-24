package com.lazysyntax.nutron.presentation.ui.features.statistics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.himanshoe.charty.color.ChartyColor
import com.himanshoe.charty.line.LineChart
import com.himanshoe.charty.line.StackedAreaChart
import com.himanshoe.charty.line.config.LineChartConfig
import com.himanshoe.charty.line.data.LineGroup
import com.lazysyntax.nutron.presentation.theme.CaloriesIndexColor
import com.lazysyntax.nutron.presentation.theme.CarbohydratesIndexColor
import com.lazysyntax.nutron.presentation.theme.FatsIndexColor
import com.lazysyntax.nutron.presentation.theme.ProteinsIndexColor
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
            val hasData = uiState.caloriesData.isNotEmpty()

            RangeSelector(
                selectedRange = uiState.selectedRange,
                onRangeSelected = { viewModel.onEvent(StatisticsEvent.OnRangeSelected(it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Consumo calórico",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start).padding(top = 16.dp)
            )
            SummaryCards(
                summaryLabel = "Calorías",
                average = uiState.averageCalories,
                total = uiState.totalCalories,
                unit = "kcal",
                color = CaloriesIndexColor
            )

            if (hasData) {
                val chartWidth = (uiState.caloriesData.size * 60).dp.coerceAtLeast(300.dp)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .horizontalScroll(rememberScrollState())
                ) {
                    LineChart(
                        color = ChartyColor.Solid(MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .width(chartWidth).padding(16.dp)
                            .fillMaxHeight(),
                        data = { uiState.caloriesData },
                        lineConfig = LineChartConfig(
                            lineWidth = 2f,
                            smoothCurve = true,
                            showPoints = true,
                        )
                    )
                }

            } else {
                Box(modifier = Modifier.height(300.dp), contentAlignment = Alignment.Center) {
                    Text(
                        "No hay datos para este periodo",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Consumo nutricional",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)) {

                SummaryCardsBox(
                    modifier = Modifier.weight(1f),
                    summaryLabel = "Proteínas",
                    average = uiState.averageProteins,
                    total = uiState.totalProteins,
                    unit = "g",
                    color = ProteinsIndexColor,
                    selectedNutrients = { uiState.selectedNutrients.contains(NutrientType.PROTEINS) },
                    onNutrientToggled = {
                        viewModel.onEvent(
                            StatisticsEvent.OnNutrientToggled(
                                NutrientType.PROTEINS
                            )
                        )
                    }
                )
                SummaryCardsBox(
                    modifier = Modifier.weight(1f),
                    summaryLabel = "Carbohidratos",
                    average = uiState.averageCarbs,
                    total = uiState.totalCarbs,
                    unit = "g",
                    color = CarbohydratesIndexColor,
                    selectedNutrients = { uiState.selectedNutrients.contains(NutrientType.CARBS) },
                    onNutrientToggled = {
                        viewModel.onEvent(
                            StatisticsEvent.OnNutrientToggled(
                                NutrientType.CARBS
                            )
                        )
                    }
                )
                SummaryCardsBox(
                    modifier = Modifier.weight(1f),
                    summaryLabel = "Grasas",
                    average = uiState.averageFats,
                    total = uiState.totalFats,
                    unit = "g",
                    color = FatsIndexColor,
                    selectedNutrients = { uiState.selectedNutrients.contains(NutrientType.FATS) },
                    onNutrientToggled = {
                        viewModel.onEvent(
                            StatisticsEvent.OnNutrientToggled(
                                NutrientType.FATS
                            )
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))





            if (hasData) {
                // Mapeamos los datos: Cada grupo es una fecha con los valores de los nutrientes seleccionados
                // Esto asegura que los puntos estén alineados por fecha en el eje X
                val chartGroups = uiState.caloriesData.indices.map { i ->
                    val label = ""
                    val values = mutableListOf<Float>()


                    if (uiState.selectedNutrients.contains(NutrientType.PROTEINS)) {
                        values.add(uiState.proteinsData[i].value)
                    }
                    if (uiState.selectedNutrients.contains(NutrientType.FATS)) {
                        values.add(uiState.fatsData[i].value)
                    }
                    if (uiState.selectedNutrients.contains(NutrientType.CARBS)) {
                        values.add(uiState.carbsData[i].value)
                    }

                    LineGroup(label, values)
                }




                if (chartGroups.isNotEmpty()) {
                    val chartWidth = (chartGroups.size * 60).dp.coerceAtLeast(300.dp)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        StackedAreaChart(
                            colors = ChartyColor.Gradient(
                                listOf(

                                    Color(0xFFE91E63),
                                    Color(0xFF2196F3),
                                    Color(0xFFFF9800),
                                )
                            ),
                            modifier = Modifier
                                .width(chartWidth)
                                .fillMaxHeight().padding(16.dp),
                            data = { chartGroups },
                            lineConfig = LineChartConfig(

                                lineWidth = 2f,
                                smoothCurve = true,
                                showPoints = true,

                                )
                        )
                    }
                }
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
fun SummaryCards(
    summaryLabel: String,
    average: Double,
    total: Double,
    unit: String,
    color: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(color, CircleShape)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = summaryLabel,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "Media",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "${average.roundToInt()} $unit",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "Total",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "${total.roundToInt()} $unit",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun SummaryCardsBox(
    modifier: Modifier = Modifier,
    summaryLabel: String,
    average: Double,
    total: Double,
    unit: String,
    color: Color,
    onNutrientToggled: () -> Unit,
    selectedNutrients: () -> Boolean
) {
    val isSelected = selectedNutrients()


    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.clip(RoundedCornerShape(16.dp)).clickable {
            onNutrientToggled()
        },

        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface
        ),

        border = BorderStroke(
            width = if (isSelected) 2.dp else 0.dp,
            brush = if (isSelected) {
                SolidColor(MaterialTheme.colorScheme.primary.copy(alpha = 0.7f))
            } else {
                SolidColor(Color.Transparent)
            }
        )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color, CircleShape)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = summaryLabel,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Media",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "${average.roundToInt()} $unit",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        "Total",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "${total.roundToInt()} $unit",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PrevCardBox() {
    Surface {
        Column() {

        }
    }
}