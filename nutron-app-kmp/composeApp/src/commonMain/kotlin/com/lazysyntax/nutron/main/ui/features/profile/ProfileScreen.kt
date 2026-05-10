package com.lazysyntax.nutron.main.ui.features.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.main.ui.navigation.composables.NavBar
import com.lazysyntax.nutron.main.ui.navigation.composables.TopAppBarCommon
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.title_profile
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = koinViewModel()
) {

    val profileState by profileViewModel.uiState.collectAsState()
    ProfileContent(
        uiState = profileState,
        profileViewModel = profileViewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    profileViewModel: ProfileViewModel,
) {
    Scaffold(
        topBar = { TopAppBarCommon(stringResource(Res.string.title_profile)) },
        bottomBar = { NavBar() }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CardProfileStat("Altura", uiState = uiState.height, unit = "cm")
            CardProfileStat("Peso", uiState = uiState.weight, unit = "kg")
            CardProfileStat("Genero", uiState = uiState.gender, unit = "")
            CardProfileStat(
                "BMI", uiState = uiState.bodyMassIndex, unit = "&",
                titleTooltip = "Body Mass Index(BMI)",
                textTooltip = "indicador simple de la relación entre el peso y la talla que se utiliza frecuentemente para identificar el sobrepeso y la obesidad en los adultos\n" +
                        "Bajo peso: IMC inferior a 18.5.\n" +
                        "Peso saludable (Normal): IMC entre 18.5 y 24.9.\n" +
                        "Sobrepeso: IMC entre 25.0 y 29.9.\n" +
                        "Obesidad: IMC de 30.0 o superior.\n" +
                        "\n" +
                        "    Clase 1: 30.0 a 34.9.\n" +
                        "    Clase 2: 35.0 a 39.9.\n" +
                        "    Clase 3 (Mórbida): 40.0 o más"
            )

            CardProfileStat(
                "RMB", uiState = uiState.basalMetabolicRate,
                unit = "Kg/m2",
                titleTooltip = "Basal Metabolic Rate (RMB)",
                textTooltip = "Cantidad mínima de calorías que tu cuerpo necesita para funcionar en reposo absoluto"
            )
            CardProfileStat(
                "Grasa corporal", uiState = uiState.bodyFatPercentage, unit = "%",
                titleTooltip = "Porcentaje de grasa corporal",
                textTooltip = " Rangos de referencia generales (porcentaje de grasa):\n" +
                        "\n" +
                        "    Mujeres esenciales: 10-13%\n" +
                        "    Mujeres atletas: 14-20%\n" +
                        "    Mujeres fitness: 21-24%\n" +
                        "    Mujeres promedio: 25-31%\n" +
                        "    Hombres esenciales: 2-5%\n" +
                        "    Hombres atletas: 6-13%\n" +
                        "    Hombres fitness: 14-17%\n" +
                        "    Hombres promedio: 18-24"
            )
            CardProfileStat("Agua corporal", uiState = uiState.bodyWaterPercentage, unit = "%")
            CardProfileStat(
                "Gasto Energético Total (GET)",
                uiState = uiState.basalMetabolicRate ,
                unit = "Kcal",
                titleTooltip = "Gasto Energético Total (GET)",
                textTooltip = "Este parámetro ajusta las calorías según tu factor de movimiento diario"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardProfileStat(
    label: String,
    uiState: String,
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
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$label: ",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "$uiState $unit",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        )
    }

}

@Preview
@Composable
fun prevCard() {
    CardProfileStat(
        label = "Label",
        uiState = "estado",
        unit = "kg",
    )
}