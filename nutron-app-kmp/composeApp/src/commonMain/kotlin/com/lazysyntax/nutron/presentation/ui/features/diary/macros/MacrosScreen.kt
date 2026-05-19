package com.lazysyntax.nutron.presentation.ui.features.diary.macros

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.presentation.ui.features.diary.DiaryViewModel
import com.lazysyntax.nutron.presentation.ui.navigation.composables.TopAppBarWhitBackButtonCommon
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.login_button_enter
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MacrosScreen(
    viewModel: DiaryViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBarWhitBackButtonCommon(
                title = uiState.selectedMeal?.name ?: "Macros",
                onBack = { viewModel.onMacrosEvent(MacrosEvent.OnClickBack) }
            )
        }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val food = uiState.macrosUiState.editedFood
            val quantityState = food.nutriments?.quantity ?: ""
            Text(text = "Product: ${food.name ?: "N/A"} \n")

            TextField(
                label = { Text(text = "QUANTITY")},
                prefix = { Text(text = food.nutriments?.quantityUnit ?: "N/A")},
                value = quantityState ,
                onValueChange = { viewModel.onMacrosEvent(MacrosEvent.QuantityChanged(it)) }
                ,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.LightGray.copy(alpha = 0.5f),
                    unfocusedContainerColor = Color.LightGray,
                    errorPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        alpha = 0.4f
                    ),
                    focusedIndicatorColor = Color.Cyan,
                    cursorColor = Color.Cyan
                )
            )

            Text(
                text = "Product Name Esp ${food.nameEs ?: "N/A"} \n"
                        + "Product Name Eng ${food.nameEn ?: "N/A"} \n"
                        + "Barcode ${food.barcode ?: "xxxx"} \n"
                        + "Calorías: ${food.nutriments?.calories ?: 0.0} kcal\n"
                        + "Fats: ${food.nutriments?.fat ?: 0.0} g\n"
                        + "- Saturated Fats: ${food.nutriments?.saturatedFat ?: 0.0} g\n"
                        + "Carbs: ${food.nutriments?.carbs ?: 0.0} g\n"
                        + "- Sugar: ${food.nutriments?.sugars ?: 0.0} g\n"
                        + "Proteins: ${food.nutriments?.proteins ?: 0.0} g\n"
                        + "Salt: ${food.nutriments?.salt ?: 0.0} g\n"
                        + "Nutriscore: ${food.nutriscoreGrade ?: "Na"} \n" +
                        "Brand: ${food.brands ?: "N/A"} \n",
                style = MaterialTheme.typography.bodySmall
            )
            HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
            Button(
                onClick = {viewModel.onMacrosEvent(MacrosEvent.OnclickSave)},
            ) {
                Text(stringResource(Res.string.login_button_enter))
            }
        }
    }
}
