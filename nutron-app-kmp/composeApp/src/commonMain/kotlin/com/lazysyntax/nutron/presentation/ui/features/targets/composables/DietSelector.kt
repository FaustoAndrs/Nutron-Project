package com.lazysyntax.nutron.presentation.ui.features.targets.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietSelector(
    sheetState: SheetState,
    scope: CoroutineScope,
    onDietSelected: (Diet) -> Unit,
    onAddCustomDiet: (Diet) -> Unit,
    onDismiss: () -> Unit,
    diets: List<Diet>,
) {
    var showCustomDialog by remember { mutableStateOf(false) }

    if (showCustomDialog) {
        CustomDietDialog(
            onDismiss = { showCustomDialog = false },
            onConfirm = { customDiet ->
                onAddCustomDiet(customDiet)
                showCustomDialog = false
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    onDismiss()
                }
            }
        )
    }

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp, top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Selecciona tu dieta",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            diets.forEach { diet ->
                DietOption(diet) {
                    onDietSelected(diet)
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        onDismiss()
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showCustomDialog = true },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Text("Crear Dieta Personalizada")
            }
        }
    }
}

@Composable
fun CustomDietDialog(
    onDismiss: () -> Unit,
    onConfirm: (Diet) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }

    val carbsInt = carbs.toIntOrNull() ?: 0
    val proteinInt = protein.toIntOrNull() ?: 0
    val fatInt = fat.toIntOrNull() ?: 0
    val total = carbsInt + proteinInt + fatInt
    val isValid = total == 100 && name.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Dieta Personalizada") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = carbs,
                        onValueChange = { if (it.length <= 3) carbs = it },
                        label = { Text("% Carbs") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = protein,
                        onValueChange = { if (it.length <= 3) protein = it },
                        label = { Text("% Prot") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = fat,
                        onValueChange = { if (it.length <= 3) fat = it },
                        label = { Text("% Fat") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                
                Text(
                    text = "Total: $total% (Debe ser 100%)",
                    color = if (total == 100) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(Diet(name, carbsInt, proteinInt, fatInt)) },
                enabled = isValid
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DietOption(diet: Diet, onDietSelected: (Diet) -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDietSelected(diet) }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                content = {
                    Text(" ${diet.name}: ", style = MaterialTheme.typography.bodyLargeEmphasized)
                    Text(
                        "Carbs: ${diet.carbs}%",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Protein: ${diet.protein}%",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text("Fat: ${diet.fat}%", style = MaterialTheme.typography.bodyMedium)
                }
            )
        })
}

@Serializable
data class Diet(val name: String, val carbs: Int, val protein: Int, val fat: Int)

@Preview
@Composable
fun PreviewDiet() {
    DietOption(Diet("Standard", 50, 20, 30), onDietSelected = {})
}