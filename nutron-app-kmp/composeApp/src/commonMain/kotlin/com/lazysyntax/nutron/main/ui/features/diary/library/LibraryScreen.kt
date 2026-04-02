package com.lazysyntax.nutron.main.ui.features.diary.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.main.ui.features.diary.DiaryViewModel
import com.lazysyntax.nutron.main.ui.navigation.TopAppBarWhitBackButtonCommon
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.diary_headline
import nutron.composeapp.generated.resources.title_library
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    onBack: () -> Unit,
    viewModel: DiaryViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBarWhitBackButtonCommon(
                title = stringResource(Res.string.title_library),
                onBack = onBack
            )
        }
    ) { padding ->
        Column {
            Text(
                stringResource(Res.string.diary_headline),
                style = MaterialTheme.typography.headlineMedium
            )
            TextField(
                label = { Text(text = "Product Name") },
                placeholder = { Text(text = "Nutella") },
                value = uiState.productName,
                onValueChange = { viewModel.onProductNameFieldChange(it) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
            IconButton(
                onClick = { viewModel.onSearchProduct() },
                content = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Product"
                    )
                }
            )

            TextField(
                label = { Text(text = "Product Barcode") },
                placeholder = { Text(text = "3017624010701") },
                value = uiState.barcode,
                onValueChange = { viewModel.onBarcodeFieldChange(it) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )

            // TODO("Implementar apertura de la camara")

            IconButton(
            onClick = {  viewModel.onSearchBarcode() },
                content = {
                    Icon(
                        imageVector = Icons.Filled.BarChart,
                        contentDescription = "Search Barcode"
                    )
                }
            )


            Column(
                modifier = Modifier.fillMaxSize().padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Lista de resultados
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .weight(1f) // weight(1f) para que ocupe el espacio sobrante
                ) {
                    uiState.productList?.let { products ->
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
                    }
                }
            }
        }
    }
}
