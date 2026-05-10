package com.lazysyntax.nutron.main.ui.features.targets.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    onDismiss: () -> Unit,
    diets: List<Diet>,
) {


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
                "Selecciona tu objetivo",
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
        }
    }
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
 class Diet(val name: String, val carbs: Int, val protein: Int, val fat: Int)

@Preview
@Composable
fun PreviewDiet() {
    DietOption(Diet("Standard", 50, 20, 30), onDietSelected = {})
}