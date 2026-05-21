package com.lazysyntax.nutron.presentation.ui.features.diary.macros

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.presentation.ui.features.diary.DiaryViewModel
import com.lazysyntax.nutron.presentation.ui.features.diary.composables.MacroProgressBar
import com.lazysyntax.nutron.presentation.ui.navigation.composables.TopAppBarWhitBackButtonCommon
import nutron.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MacrosScreen(
    viewModel: DiaryViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBarWhitBackButtonCommon(
                title = uiState.selectedMeal?.name ?: "Detalle del Producto",
                onBack = { viewModel.onMacrosEvent(MacrosEvent.OnClickBack) }
            )
        }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                }
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val food = uiState.macrosUiState.editedFood
            val nutriments = food.nutriments

            // Header con nombre y marca
            Column(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = food.name ?: "Producto",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                if (!food.brands.isNullOrBlank()) {
                    Text(
                        text = food.brands,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            // Card para ajustar cantidad
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = nutriments?.quantity ?: "",
                        onValueChange = { viewModel.onMacrosEvent(MacrosEvent.QuantityChanged(it)) },
                        label = { Text("Cantidad") },
                        suffix = { Text(nutriments?.quantityUnit ?: "g") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        singleLine = true,
                    )
                    Text(
                        text = "Ajusta la cantidad para ver los macros",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Resumen de Macros
            Text(
                text = "Información Nutricional",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                fontWeight = FontWeight.Bold
            )

            MacroProgressBar(
                label = "Calorías",
                current = nutriments?.calories ?: 0.0,
                target = 2000.0, // Solo como referencia visual o podrías usar el target real del usuario
                unit = "kcal",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MacroProgressBar(
                    label = "Prot",
                    current = nutriments?.proteins ?: 0.0,
                    target = 150.0,
                    unit = "g",
                    color = androidx.compose.ui.graphics.Color(0xFFE57373),
                    modifier = Modifier.weight(1f)
                )
                MacroProgressBar(
                    label = "Carbs",
                    current = nutriments?.carbs ?: 0.0,
                    target = 250.0,
                    unit = "g",
                    color = androidx.compose.ui.graphics.Color(0xFFFFB74D),
                    modifier = Modifier.weight(1f)
                )
                MacroProgressBar(
                    label = "Grasas",
                    current = nutriments?.fat ?: 0.0,
                    target = 70.0,
                    unit = "g",
                    color = androidx.compose.ui.graphics.Color(0xFF81C784),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Otros detalles
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    DetailRow("Grasas Saturadas", "${nutriments?.saturatedFat ?: 0.0} g")
                    DetailRow("Azúcares", "${nutriments?.sugars ?: 0.0} g")
                    DetailRow("Sal", "${nutriments?.salt ?: 0.0} g")
                    DetailRow("Código de barras", food.barcode ?: "Desconocido")
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.onMacrosEvent(MacrosEvent.OnclickSave) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Guardar Producto", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}
