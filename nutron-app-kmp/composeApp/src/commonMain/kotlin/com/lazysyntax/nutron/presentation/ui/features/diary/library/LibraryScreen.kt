package com.lazysyntax.nutron.presentation.ui.features.diary.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.domain.models.Food
import com.lazysyntax.nutron.presentation.theme.Theme
import com.lazysyntax.nutron.presentation.ui.features.diary.DiaryViewModel
import com.lazysyntax.nutron.presentation.ui.features.diary.DiaryViewModel.SearchSource
import com.lazysyntax.nutron.presentation.ui.features.diary.composables.LibrarySearchBar
import com.lazysyntax.nutron.presentation.ui.features.diary.composables.LibraryTopAppBar
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.camera.CAMERA
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.button_back
import nutron.composeapp.generated.resources.library_error_barcode_not_found
import nutron.composeapp.generated.resources.library_error_scanner_failed
import nutron.composeapp.generated.resources.library_no_results
import nutron.composeapp.generated.resources.library_search_placeholder
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
        onProductSelected = { viewModel.onLibraryEvent(LibraryEvent.ProductSelected(it)) },
        onSearchSourceChanged = { viewModel.onLibraryEvent(LibraryEvent.SearchSourceChanged(it)) },
        onError = { viewModel.onLibraryEvent(LibraryEvent.OnError(it)) }
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
    onProductSelected: (Food) -> Unit,
    onSearchSourceChanged: (SearchSource) -> Unit,
    onError: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var showBarBarcodeSearch = rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Manejo de errores mediante Snackbar
    val barcodeNotFoundMsg = stringResource(Res.string.library_error_barcode_not_found)
    val scannerFailedMsg = stringResource(Res.string.library_error_scanner_failed)

    LaunchedEffect(uiState.error) {
        if (uiState.error.isNotEmpty()) {
            val message = when (uiState.error) {
                "barcode_not_found" -> barcodeNotFoundMsg
                "scanner_failed" -> scannerFailedMsg
                else -> uiState.error
            }
            snackbarHostState.showSnackbar(message)
            onError("") // Limpiar error tras mostrarlo
        }
    }

    Scaffold(
        topBar = {
            LibraryTopAppBar(
                title = stringResource(Res.string.title_library),
                onBack = onBack,
                onSearch = {
                    onSearchProduct()
                    focusManager.clearFocus()
                },
                onCleanSearch = { onProductChanged("") },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxHeight().padding(padding)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                }, verticalArrangement = Arrangement.Top
        ) {
            LibrarySearchBar(
                query = uiState.productName,
                onSearch = {
                    onSearchProduct()
                    focusManager.clearFocus()
                },
                onScanBarcode = onShowScanner,
                onQueryChanged = onProductChanged,
                onCleanQuery = { onProductChanged("") },
                placeholder = "Search"
            )

            if (showBarBarcodeSearch.value) {
                LibrarySearchBar(
                    query = uiState.barcode,
                    onSearch = {
                        onSearchBarcode()
                        focusManager.clearFocus()
                    },
                    onScanBarcode = onShowScanner,
                    onQueryChanged = onBarcodeChanded,
                    onCleanQuery = { onBarcodeChanded("") },
                    placeholder = "Search by Barcode"
                )
            }

            // 2. Selectores de origen (Chips)
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 32.dp, end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.searchSource == SearchSource.LOCAL,
                    onClick = {
                        onSearchSourceChanged(SearchSource.LOCAL)
                        focusManager.clearFocus()
                    },
                    label = { Text("Mi Librería") },
                    shape = RoundedCornerShape(20.dp),
                    leadingIcon = if (uiState.searchSource == SearchSource.LOCAL) {
                        {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null)

                FilterChip(
                    selected = uiState.searchSource == SearchSource.API,
                    onClick = {
                        onSearchSourceChanged(SearchSource.API)
                        if (uiState.productName.isNotBlank())onSearchProduct()
                        focusManager.clearFocus()
                    },
                    label = { Text("Buscar en la Red") },
                    shape = RoundedCornerShape(20.dp),
                    leadingIcon = if (uiState.searchSource == SearchSource.API) {
                        {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else null)
                IconButton(
                    modifier = Modifier.padding(0.dp),
                    onClick = { showBarBarcodeSearch.value = !showBarBarcodeSearch.value },
                    content = {
                        if (showBarBarcodeSearch.value) Icon(
                            imageVector = Icons.Default.ExpandLess,
                            contentDescription = stringResource(Res.string.button_back)
                        )
                        else Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = stringResource(Res.string.button_back)
                        )
                    })
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize().weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    val results = uiState.foodListResult

                    if (results == null) {
                        // Estado inicial: El usuario aún no ha realizado una búsqueda
                        Box(
                            modifier = Modifier.fillMaxSize().weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.library_search_placeholder),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else if (results.isEmpty()) {
                        // Caso: Se realizó la búsqueda pero no hubo coincidencias
                        Box(
                            modifier = Modifier.fillMaxSize().weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(Res.string.library_no_results),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        // Lista de resultados
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                                .weight(1f) // weight(1f) para que ocupe el espacio sobrante
                        ) {
                            items(results) { product ->
                                Column(modifier = Modifier.fillMaxWidth().clickable {
                                    onProductSelected(product)
                                    focusManager.clearFocus()
                                }.padding(horizontal = 16.dp, vertical = 12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = product.name ?: "Producto sin nombre",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            if (!product.brands.isNullOrBlank()) {
                                                Text(
                                                    text = product.brands,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.secondary
                                                )
                                            }
                                        }

                                        Text(
                                            text = "${product.nutriments?.calories ?: 0.0} kcal",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        MacroMiniItem(
                                            "P",
                                            "${product.nutriments?.proteins ?: 0.0}g",
                                            Color(0xFFE57373)
                                        )
                                        MacroMiniItem(
                                            "C",
                                            "${product.nutriments?.carbs ?: 0.0}g",
                                            Color(0xFFFFB74D)
                                        )
                                        MacroMiniItem(
                                            "G",
                                            "${product.nutriments?.fat ?: 0.0}g",
                                            Color(0xFF81C784)
                                        )
                                    }

                                    HorizontalDivider(
                                        modifier = Modifier.padding(top = 12.dp),
                                        thickness = 0.5.dp,
                                        color = MaterialTheme.colorScheme.outlineVariant
                                    )
                                }
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
                headerTitle = "Scan Barcode", showTorch = true, showZoom = false
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
                    onError("scanner_failed")
                }

                BarcodeResult.OnCanceled -> {
                    println("Canceled")
                    onShowScanner()
                }
            }
        }

    }
}

@Composable
fun MacroMiniItem(label: String, value: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(8.dp).background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$label: $value",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PreviewLibraryScafold() {
    Theme {
        var libraryUiState = LibraryUiState()
        LibraryContent(
            uiState = libraryUiState,
            onBack = {},
            onSearchBarcode = {},
            onSearchProduct = {},
            onBarcodeChanded = {},
            onProductChanged = {},
            showScanner = false,
            hasCameraPermission = true,
            onShowScanner = {},
            onProductSelected = {},
            onSearchSourceChanged = {},
            onError = {}
        )
    }
}