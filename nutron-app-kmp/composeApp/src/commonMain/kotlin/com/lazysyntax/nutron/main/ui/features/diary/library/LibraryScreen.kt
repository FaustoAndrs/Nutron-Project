package com.lazysyntax.nutron.main.ui.features.diary.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.main.ui.features.diary.composables.LibrarySearchBar
import com.lazysyntax.nutron.main.ui.features.diary.composables.LibraryTopAppBar
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.camera.CAMERA
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.barcode_scanner_24px
import nutron.composeapp.generated.resources.title_library
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import org.ncgroup.kscan.BarcodeFormat
import org.ncgroup.kscan.BarcodeResult
import org.ncgroup.kscan.ScannerView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = koinViewModel(),
    onLibraryEvent: (LibraryEvent) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LibraryContent(
        uiState = uiState,
        viewModel = viewModel,
        onBack = { onLibraryEvent(LibraryEvent.OnClickBack) },
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryContent(
    uiState: LibraryUiState,
    viewModel: LibraryViewModel,
    onBack: () -> Unit,

){
    var showScanner by remember { mutableStateOf(false) }

    // Configuración de MOKO Permissions
    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) { factory.createPermissionsController() }
    BindEffect(controller)

    var hasCameraPermission by remember { mutableStateOf(false) }

    // LaunchedEffect que solicita el permiso cuando se intenta mostrar el escáner
    LaunchedEffect(showScanner) {
        if (showScanner) {
            try {
                controller.providePermission(Permission.CAMERA)
                hasCameraPermission = true
            } catch (e: Exception) {
                showScanner = false
                hasCameraPermission = false
                println("Permiso de cámara denegado: ${e.message}")
            }
        }
    }
    if (showScanner && hasCameraPermission) {
        ScannerView(
            codeTypes = listOf(BarcodeFormat.FORMAT_QR_CODE, BarcodeFormat.FORMAT_EAN_13),
            scannerUiOptions = null
        ) { result ->
            when (result) {
                is BarcodeResult.OnSuccess -> {
                    println("Barcode: ${result.barcode.data}")
                    viewModel.onBarcodeFieldChange(result.barcode.data)
                    viewModel.onSearchBarcode()
                    showScanner = false
                }

                is BarcodeResult.OnFailed -> {
                    println("Error: ${result.exception.message}")
                }

                BarcodeResult.OnCanceled -> {
                    println("Canceled")
                    showScanner = false
                }
            }
        }

    } else {
        Scaffold(
            topBar = {
                LibraryTopAppBar(
                    title = stringResource(Res.string.title_library),
                    onBack = onBack,
                    onSearch = {},
                    onCleanSearch = {},
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
                    verticalAlignment = Alignment.CenterVertically,
                    content = {
                        LibrarySearchBar(
                            modifier = Modifier.weight(1f),
                            textFieldState = TextFieldState(),
                            onSearch = { },
                        )
                        IconButton(onClick = { showScanner = true }) {
                            Icon(
                                imageVector = vectorResource(Res.drawable.barcode_scanner_24px),
                                contentDescription = "Escanear código"
                            )
                        }
                    }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                    .semantics { isTraversalGroup = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    label = { Text(text = "Product Name") },
                    placeholder = { Text(text = "Nutella") },
                    value = uiState.productName,
                    onValueChange = { viewModel.onProductNameFieldChange(it) },
                )
                IconButton(onClick = { viewModel.onSearchProduct() }, content = {
                    Icon(
                        imageVector = Icons.Filled.Search, contentDescription = "Search Product"
                    )
                })
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
                    onValueChange = { viewModel.onBarcodeFieldChange(it) },
                )

                IconButton(onClick = { viewModel.onSearchBarcode() }, content = {
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
                    uiState.foodList?.let { products ->
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
