package com.lazysyntax.nutron.presentation.ui.features.targets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.unit.dp
import com.himanshoe.charty.color.ChartyColor
import com.himanshoe.charty.pie.PieChart
import com.himanshoe.charty.pie.config.LabelConfig
import com.himanshoe.charty.pie.config.PieChartConfig
import com.himanshoe.charty.pie.data.PieData
import com.lazysyntax.nutron.presentation.theme.CarbohydratesIndexColor
import com.lazysyntax.nutron.presentation.theme.FatsIndexColor
import com.lazysyntax.nutron.presentation.theme.ProteinsIndexColor
import com.lazysyntax.nutron.presentation.ui.features.targets.composables.CardDietStat
import com.lazysyntax.nutron.presentation.ui.features.targets.composables.CardTargetStat
import com.lazysyntax.nutron.presentation.ui.features.targets.composables.Diet
import com.lazysyntax.nutron.presentation.ui.features.targets.composables.DietSelector
import com.lazysyntax.nutron.presentation.ui.features.targets.composables.MacroDetailRow
import com.lazysyntax.nutron.presentation.ui.navigation.composables.BottomNavBar
import com.lazysyntax.nutron.presentation.ui.navigation.composables.TopAppBarCommon
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.button_back
import nutron.composeapp.generated.resources.label_carbs
import nutron.composeapp.generated.resources.label_daily_kcal
import nutron.composeapp.generated.resources.label_fats
import nutron.composeapp.generated.resources.label_macro_distribution
import nutron.composeapp.generated.resources.label_proteins
import nutron.composeapp.generated.resources.profile
import nutron.composeapp.generated.resources.profile_show_progres
import nutron.composeapp.generated.resources.title_targets
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TargetTopAppBar(
    title: String,
    onNavigationClick: () -> Unit,
) = TopAppBarCommon(
    title = title, navigationIcon = {
        IconButton(
            onClick = onNavigationClick, content = {
                Icon(
                    imageVector = vectorResource(Res.drawable.profile),
                    contentDescription = stringResource(Res.string.button_back)
                )
            })
    })

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TargetsScreen(
    viewModel: TargetsViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val diets by viewModel.diets.collectAsState()

    TargetsContent(
        fats = uiState.fats,
        proteins = uiState.proteins,
        carbs = uiState.carbs,
        dailyKcals = uiState.dailyKcal,
        onDietChanged = { viewModel.onTargetsEvent(TargetsEvent.OnSelectDietType(it)) },
        onAddCustomDiet = { viewModel.onTargetsEvent(TargetsEvent.OnAddCustomDiet(it)) },
        diets = diets,
        selectedDiet = uiState.diet,
        onNavigateToStatistics = { viewModel.onTargetsEvent(TargetsEvent.OnNavigateToStatistics) },
        onNavigateToProfile = { viewModel.onTargetsEvent(TargetsEvent.OnNavigateToProfile) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TargetsContent(
    dailyKcals: String,
    onDietChanged: (Diet) -> Unit,
    onAddCustomDiet: (Diet) -> Unit,
    diets: List<Diet>,
    selectedDiet: Diet,
    fats: Int?,
    proteins: Int?,
    carbs: Int?,
    onNavigateToStatistics: () -> Unit,
    onNavigateToProfile: () -> Unit

) {
    val scope = rememberCoroutineScope()

    var showDietSheet by remember { mutableStateOf(false) }
    val goalSheetState = rememberModalBottomSheetState()


    Scaffold(
        topBar = { TargetTopAppBar(stringResource(Res.string.title_targets), onNavigateToProfile) },
        bottomBar = { BottomNavBar() },
        containerColor = MaterialTheme.colorScheme.background
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
                modifier = Modifier.padding(top = 8.dp),
                onClick = {
                    showDietSheet = true
                }
            )

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth().padding(8.dp),
            ) {
                Column(

                    modifier = Modifier.background(color = Color.Transparent).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    Text(
                        text = stringResource(Res.string.label_macro_distribution),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    val proteinsLabel = stringResource(Res.string.label_proteins)
                    val carbsLabel = stringResource(Res.string.label_carbs)
                    val fatsLabel = stringResource(Res.string.label_fats)

                    PieChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        config = PieChartConfig(
                            labelConfig = LabelConfig(
                                shouldShowLabels = true,
                                shouldShowLabelsOutside = true,
                                labelTextStyle = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.secondary
                                ),
                                minimumPercentageToShowLabel = 0f
                            )
                        ),
                        data = {
                            listOf(
                                PieData(proteinsLabel, selectedDiet.protein.toFloat()),
                                PieData(carbsLabel, selectedDiet.carbs.toFloat()),
                                PieData(fatsLabel, selectedDiet.fat.toFloat()),
                            )
                        },
                        color = ChartyColor.Gradient(
                            listOf(ProteinsIndexColor, CarbohydratesIndexColor, FatsIndexColor)
                        ),
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    MacroDetailRow(
                        label = stringResource(Res.string.label_proteins),
                        kcal = proteins ?: 0,
                        grams = if (proteins != null) proteins / 4 else 0,
                        color = ProteinsIndexColor
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        thickness = 0.5.dp
                    )
                    MacroDetailRow(
                        label = stringResource(Res.string.label_carbs),
                        kcal = carbs ?: 0,
                        grams = if (carbs != null) carbs / 4 else 0,
                        color = CarbohydratesIndexColor
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        thickness = 0.5.dp
                    )
                    MacroDetailRow(
                        label = stringResource(Res.string.label_fats),
                        kcal = fats ?: 0,
                        grams = if (fats != null) fats / 9 else 0,
                        color = FatsIndexColor
                    )


                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onNavigateToStatistics,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(stringResource(Res.string.profile_show_progres))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
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