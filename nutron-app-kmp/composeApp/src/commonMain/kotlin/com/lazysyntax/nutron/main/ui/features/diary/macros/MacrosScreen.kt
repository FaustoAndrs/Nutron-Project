package com.lazysyntax.nutron.main.ui.features.diary.macros

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.main.ui.navigation.composables.TopAppBarWhitBackButtonCommon
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MacrosScreen(
    onBack: () -> Unit,
    viewModel: MacrosViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBarWhitBackButtonCommon(
                title = uiState.productName,
                onBack = onBack
            )
        }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                    .semantics { isTraversalGroup = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Proteínas")
                TextField(
                    value = "uiState.food?.nutriments?.proteins" ?: "",
                    onValueChange = {  },
                )
                IconButton(onClick = {  }, content = {
                    Icon(
                        imageVector = Icons.Filled.Search, contentDescription = "Search Product"
                    )
                })
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                .semantics { isTraversalGroup = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                label = { Text(text = "Product Barcode") },
                placeholder = { Text(text = "3017624010701") },
                value = uiState.barcode,
                onValueChange = { },
            )

            IconButton(onClick = {  }, content = {
                Icon(
                    imageVector = Icons.Filled.BarChart,
                    contentDescription = "Search Barcode"
                )
            })
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.food != null) {
                Text(
                    text = "Product: ${uiState.food!!.name ?: "N/A"} \n"
                            + "Product Name Esp ${uiState.food!!.nameEs ?: "N/A"} \n"
                            + "Product Name Eng ${uiState.food!!.nameEn ?: "N/A"} \n"
                            + "Calorías: ${uiState.food!!.nutriments?.calories ?: 0.0} kcal\n" + "Fats: ${uiState.food!!.nutriments?.fat ?: 0.0} g\n" + "- Saturated Fats: ${uiState.food!!.nutriments?.saturatedFat ?: 0.0} g\n" + "Carbs: ${uiState.food!!.nutriments?.carbs ?: 0.0} g\n" + "- Sugar: ${uiState.food!!.nutriments?.sugars ?: 0.0} g\n" + "Proteins: ${uiState.food!!.nutriments?.proteins ?: 0.0} g\n" + "Salt: ${uiState.food!!.nutriments?.salt ?: 0.0} g\n"
                            + "Nutriscore: ${uiState.food!!.nutriscoreGrade ?: "Na"} \n" +
                            "Brand: ${uiState.food!!.brands ?: "N/A"} \n",
                    style = MaterialTheme.typography.bodySmall
                )
                HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
            }

            // Lista de resultados
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .weight(1f) // weight(1f) para que ocupe el espacio sobrante
            ) {
          /*      uiState.foodList?.let { products ->
                    items(products) { product ->
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(16.dp)

                        ) {
                            Text(
                                text = product.name ?: "Producto sin nombre",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Calorías: ${product.nutriments?.carbs ?: 0.0} g" + "Calorías: ${product.nutriments?.calories ?: 0.0} kcal" + "Calorías: ${product.nutriments?.proteins ?: 0.0} g" + "Calorías: ${product.nutriments?.fat ?: 0.0} g",
                                style = MaterialTheme.typography.bodySmall
                            )
                            HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                }*/
            }
        }
    }
}
