package com.lazysyntax.nutron.presentation.ui.features.targets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.himanshoe.charty.color.ChartyColor
import com.himanshoe.charty.line.AreaChart
import com.himanshoe.charty.line.config.LineChartConfig
import com.himanshoe.charty.line.data.LineData
import com.himanshoe.charty.pie.PieChart
import com.himanshoe.charty.pie.config.LabelConfig
import com.himanshoe.charty.pie.config.PieChartConfig
import com.himanshoe.charty.pie.data.PieData
import com.lazysyntax.nutron.presentation.ui.features.targets.composables.Diet
import com.lazysyntax.nutron.presentation.ui.features.targets.composables.DietSelector
import com.lazysyntax.nutron.presentation.ui.navigation.composables.NavBar
import com.lazysyntax.nutron.presentation.ui.navigation.composables.TopAppBarCommon
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.label_daily_kcal
import nutron.composeapp.generated.resources.label_diet_type
import nutron.composeapp.generated.resources.title_targets
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TargetsScreen(
    viewModel: TargetsViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val diets by viewModel.diets.collectAsState()

    TargetsContent(
        dailyKcals = uiState.dailyKcal,
        onDietChanged = { viewModel.onTargetsEvent(TargetsEvent.OnSelectDietType(it)) },
        onAddCustomDiet = { viewModel.onTargetsEvent(TargetsEvent.OnAddCustomDiet(it)) },
        diets = diets,
        selectedDiet = uiState.diet
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TargetsContent(
    dailyKcals: String,
    onDietChanged: (Diet) -> Unit,
    onAddCustomDiet: (Diet) -> Unit,
    diets: List<Diet>,
    selectedDiet: Diet
) {
    val scope = rememberCoroutineScope()

    var showDietSheet by remember { mutableStateOf(false) }
    val goalSheetState = rememberModalBottomSheetState()


    Scaffold(
        topBar = { TopAppBarCommon(stringResource(Res.string.title_targets)) },
        bottomBar = { NavBar() }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardTargetStat(
                label = stringResource(Res.string.label_daily_kcal),
                kCals = dailyKcals,
                unit = "Kcal"
            )
            CardDietStat(
                label = selectedDiet.name,
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    onDietChanged(selectedDiet)
                    showDietSheet = true
                }
            )
            PieChart(
                modifier = Modifier.fillMaxWidth().height(300.dp),
                config = PieChartConfig(
                    labelConfig = LabelConfig(
                        shouldShowLabels = true,
                        shouldShowLabelsOutside = true,
                        labelTextStyle = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.secondary
                        ),
                        // Aseguramos que se muestren incluso si el porcentaje es bajo
                        minimumPercentageToShowLabel = 0f
                    )
                ),
                data = {
                    listOf(
                        PieData("Proteins", selectedDiet.protein.toFloat()),
                        PieData("Carbs", selectedDiet.carbs.toFloat()),
                        PieData("Fats", selectedDiet.fat.toFloat()),
                    )
                },
                color = ChartyColor.Gradient(
                    listOf(Color(0xFFE91E63), Color(0xFF2196F3), Color(0xFF4CAF50))
                ),

                )

            Text(
                text = "Variation across Diets (%)",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
            )

            Text("Proteins", style = MaterialTheme.typography.labelMedium)
            AreaChart(
                modifier = Modifier.fillMaxWidth().height(150.dp).padding(8.dp),
                data = { diets.map { LineData(it.name, it.protein.toFloat()) } },
                color = ChartyColor.Gradient(listOf(Color(0xFFE91E63), Color.Transparent)),
                lineConfig = LineChartConfig(
                    showPoints = false,
                    smoothCurve = true
                )
            )

            Text("Carbs", style = MaterialTheme.typography.labelMedium)
            AreaChart(
                modifier = Modifier.fillMaxWidth().height(150.dp).padding(8.dp),
                data = { diets.map { LineData(it.name, it.carbs.toFloat()) } },
                color = ChartyColor.Gradient(listOf(Color(0xFF2196F3), Color.Transparent))
            )

            Text("Fats", style = MaterialTheme.typography.labelMedium)
            AreaChart(
                modifier = Modifier.fillMaxWidth().height(150.dp).padding(8.dp),
                data = { diets.map { LineData(it.name, it.fat.toFloat()) } },
                color = ChartyColor.Gradient(listOf(Color(0xFF4CAF50), Color.Transparent))
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

        if (showDietSheet) {
            DietSelector(
                sheetState = goalSheetState,
                scope = scope,
                onDietSelected = onDietChanged,
                onAddCustomDiet = onAddCustomDiet,
                onDismiss = { showDietSheet = false },
                diets = diets
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDietStat(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth().padding(8.dp)
            .clickable(
                onClick = onClick
            ),
        content = {

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(Res.string.label_diet_type) + ":",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardTargetStat(
    label: String,
    kCals: String,
    titleTooltip: String = "",
    textTooltip: String = "",
    unit: String,
    modifier: Modifier = Modifier
) {

    TooltipBox(
        modifier = modifier,
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            positioning = TooltipAnchorPosition.Right,
            spacingBetweenTooltipAndAnchor = 4.dp
        ),
        tooltip = {
            RichTooltip(
                title = { Text(titleTooltip) },
                text = { Text(textTooltip) }
            )
        },
        state = rememberTooltipState()
    ) {

        ElevatedCard(
            modifier = modifier.fillMaxWidth().padding(8.dp),
            content = {

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "$kCals $unit",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        )
    }

}

@Preview
@Composable
fun PrevCard() {
    Surface {
        PieChart(
            modifier = Modifier.fillMaxSize(), // Es recomendable darle un tamaño

            config = PieChartConfig(
                labelConfig = LabelConfig(
                    shouldShowLabels = true,
                    shouldShowValue = true,
                    shouldShowLabelsOutside = true,
                    labelTextStyle = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.secondary
                    ),

                    // Aseguramos que se muestren incluso si el porcentaje es bajo
                    minimumPercentageToShowLabel = 0f
                )
            ),
            data = {
                listOf(
                    PieData("Proteins", 30f),
                    PieData("Carbs", 20f),
                    PieData("Fats", 50f),
                )
            },
            color = ChartyColor.Gradient(
                listOf(Color(0xFFE91E63), Color(0xFF2196F3), Color(0xFF4CAF50))
            ),

            )
    }
    /*CardTargetStat(
        label = "Daily Caloric Requirement",
        kCals = "2038",
        unit = "Kcal",
    )*/
}