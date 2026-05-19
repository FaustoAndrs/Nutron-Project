package com.lazysyntax.nutron.presentation.ui.features.diary.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.presentation.ui.features.diary.DiaryViewModel
import com.lazysyntax.nutron.presentation.ui.features.diary.composables.LibrarySearchBar
import com.lazysyntax.nutron.presentation.ui.features.diary.composables.LibraryTopAppBar
import com.lazysyntax.nutron.domain.models.Food
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.camera.CAMERA
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.title_library
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.ncgroup.kscan.BarcodeFormat
import org.ncgroup.kscan.BarcodeResult
import org.ncgroup.kscan.ScannerColors
import org.ncgroup.kscan.ScannerUiOptions
import org.ncgroup.kscan.ScannerView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: DiaryViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

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

    LibraryContent(
        uiState = uiState.libraryUiState,
        onBack = { viewModel.onLibraryEvent(LibraryEvent.OnClickBack) },
        onSearchBarcode = { viewModel.onLibraryEvent(LibraryEvent.OnClickSearchBarcode) },
        onSearchProduct = { viewModel.onLibraryEvent((LibraryEvent.OnClickSearchProduct)) },
        onBarcodeChanded = { viewModel.onLibraryEvent((LibraryEvent.BarcodeChanged(it))) },
        onProductChanged = { viewModel.onLibraryEvent((LibraryEvent.ProductNameChanged(it))) },
        showScanner = showScanner,
        hasCameraPermission = hasCameraPermission,
        onShowScanner = { showScanner = !showScanner },
        onProductSelected = {viewModel.onLibraryEvent(LibraryEvent.ProductSelected(it))}


    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryContent(
    uiState: LibraryUiState,
    onBack: () -> Unit,
    onSearchBarcode: () -> Unit,
    onSearchProduct: () -> Unit,
    onBarcodeChanded: (String) -> Unit,
    onProductChanged: (String) -> Unit,
    showScanner: Boolean,
    hasCameraPermission: Boolean,
    onShowScanner: () -> Unit,
    onProductSelected: (Food) -> Unit
) {
    Scaffold(
        topBar = {
            LibraryTopAppBar(
                title = stringResource(Res.string.title_library),
                onBack = onBack,
                onSearch = onSearchProduct,
                onCleanSearch = { onProductChanged("") },
            )
        }) { padding ->
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LibrarySearchBar(
                query = uiState.productName,
                onSearch = onSearchProduct,
                onScanBarcode = onSearchBarcode,
                onQueryChanged = onProductChanged ,
                onCleanQuery = { onProductChanged("") },
            )
            LibrarySearchBar(
                query = uiState.barcode,
                onSearch = onSearchBarcode,
                onScanBarcode = onSearchBarcode,
                onQueryChanged = onBarcodeChanded,
                onCleanQuery = { onProductChanged("") },
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Lista de resultados
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .weight(1f) // weight(1f) para que ocupe el espacio sobrante
                ) {
                    uiState.foodListResult?.let { products ->
                        items(products) { product ->
                            Column(
                                modifier = Modifier.fillMaxWidth()
                                    .clickable { onProductSelected(product) }
                                    .padding(16.dp)

                            ) {
                                Text(
                                    text = product.name ?: "Producto sin nombre",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Calorías: ${product.nutriments?.carbs ?: 0.0} g"
                                            + "Calorías: ${product.nutriments?.calories ?: 0.0} kcal"
                                            + "Calorías: ${product.nutriments?.proteins ?: 0.0} g"
                                            + "Calorías: ${product.nutriments?.fat ?: 0.0} g",
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

    if (showScanner && hasCameraPermission) {
        ScannerView(
            codeTypes = listOf(BarcodeFormat.FORMAT_QR_CODE, BarcodeFormat.FORMAT_EAN_13),
            scannerUiOptions = ScannerUiOptions().copy(
                headerTitle = "Scan Barcode",
                showTorch = true,
                showZoom = false
            ),
            colors = ScannerColors().copy(
                headerContainerColor = Color.Transparent
            )

        ) { result ->
            when (result) {
                is BarcodeResult.OnSuccess -> {
                    println("Barcode: ${result.barcode.data}")
                    onBarcodeChanded(result.barcode.data)
                    onSearchBarcode()
                    onShowScanner()
                }

                is BarcodeResult.OnFailed -> {
                    println("Error: ${result.exception.message}")
                }

                BarcodeResult.OnCanceled -> {
                    println("Canceled")
                    onShowScanner()
                }
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PreviewLibraryScafold() {
    Scaffold(
        topBar = {
            LibraryTopAppBar(
                title = stringResource(Res.string.title_library),
                onBack = { },
                onSearch = { },
                onCleanSearch = { },
            )
        }) { padding ->
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LibrarySearchBar(
                query = "",
                onSearch = { },
                onScanBarcode = { },
                onQueryChanged = {},
                onCleanQuery = {},
            )
            /*Row(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    LibrarySearchBar(
                        textFieldState = TextFieldState(),
                        onSearch = { },
                        onScanBarcode = { }
                    )
                }
            )*/

        }
    }
}