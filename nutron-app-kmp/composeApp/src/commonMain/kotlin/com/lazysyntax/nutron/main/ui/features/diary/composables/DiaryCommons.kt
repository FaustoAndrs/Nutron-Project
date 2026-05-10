package com.lazysyntax.nutron.main.ui.features.diary.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.More
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.main.ui.features.diary.DiaryEvent
import com.lazysyntax.nutron.main.ui.features.diary.DiaryUiState
import com.lazysyntax.nutron.models.Food
import com.lazysyntax.nutron.models.Meal
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.barcode_24px
import nutron.composeapp.generated.resources.button_back
import nutron.composeapp.generated.resources.empty_meal
import nutron.composeapp.generated.resources.library_button_add_product
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryTopAppBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    onBack: () -> Unit,
    onSearch: () -> Unit,
    onCleanSearch: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge
            )
        }, navigationIcon = {
            IconButton(
                onClick = onBack, content = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = stringResource(Res.string.button_back)
                    )
                })
        }, actions = {

            IconButton(onClick = onSearch) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    //contentDescription = stringResource(Res.string.button_back)
                    contentDescription = "Buscar"
                )
            }
            IconButton(onClick = onCleanSearch) {
                Icon(
                    imageVector = vectorResource(Res.drawable.barcode_24px),
                    contentDescription = "Escanear código de barras"
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.More,
                    contentDescription = "Mas opciones"
                )
            }
        }, scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryTopAppBar(
    uiState: DiaryUiState,
    onEvent: (DiaryEvent) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    title: String
) {
    val datePickerState = rememberDatePickerState()


    CenterAlignedTopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onEvent(DiaryEvent.OnClickChangeDate) }
            ) {
                Text(
                    text = uiState.date.toString(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Cambiar fecha",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { onEvent(DiaryEvent.OnClickPreviousDay) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = "Día anterior"
                )
            }
        },
        actions = {
            IconButton(onClick = { onEvent(DiaryEvent.OnClickNextDay) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Día siguiente"
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.More,
                    contentDescription = "Más opciones"
                )
            }
        },
        scrollBehavior = scrollBehavior
    )

    if (uiState.isDatePickerVisible) {
        DatePickerModal(
            onDateSelected = {
                onEvent(DiaryEvent.OnDateSelected(datePickerState.selectedDateMillis))
            },
            onDismiss = { onEvent(DiaryEvent.OnDismissDatePicker) },
            datePickerState = datePickerState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: () -> Unit,
    onDismiss: () -> Unit,
    datePickerState: DatePickerState,
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun ProductCard(food: Food, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth().padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Product: ${food.name ?: "N/A"}")
                Text(text = "Brand: ${food.brands ?: "N/A"}")
            }
            Text(
                text = "C: ${food.nutriments?.calories ?: 0.0} kcal "
                        + "- P: ${food.nutriments?.proteins ?: 0.0} g"
                        + "- C: ${food.nutriments?.carbs ?: 0.0} g"
                        + "- F: ${food.nutriments?.fat ?: 0.0} g"
            )
        }

    }
}

@Composable
fun ProductCardDetailed(food: Food, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth().padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            Text(text = "Product: ${food.name ?: "N/A"}")

            Text(
                text = " \n" + "Product Name Esp ${food.nameEs ?: "N/A"} \n"
                        + "Product Name Eng ${food.nameEn ?: "N/A"} \n"
                        + "Calorías: ${food.nutriments?.calories ?: 0.0} kcal\n"
                        + "Fats: ${food.nutriments?.fat ?: 0.0} g\n"
                        + "- Saturated Fats: ${food.nutriments?.saturatedFat ?: 0.0} g\n"
                        + "Carbs: ${food.nutriments?.carbs ?: 0.0} g\n"
                        + "- Sugar: ${food.nutriments?.sugars ?: 0.0} g\n"
                        + "Proteins: ${food.nutriments?.proteins ?: 0.0} g\n"
                        + "Salt: ${food.nutriments?.salt ?: 0.0} g\n"
                        + "Nutriscore: ${food.nutriscoreGrade ?: "Na"} \n"
                        + "Brand: ${food.brands ?: "N/A"} \n",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun MealCard(
    meal: Meal,
    modifier: Modifier = Modifier,
    onAddProduct: () -> Unit,
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth().padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            // Título de la comida (ej. Desayuno)
            Text(
                text = meal.name ?: "Sin nombre",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Listado de productos
            if (meal.foods.isNullOrEmpty()) {
                Text(
                    text = stringResource(Res.string.empty_meal),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                meal.foods.forEach { product ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = "• ${product.name ?: "Producto desconocido"}",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant
            )
            //TODO("onClick = onAddProduct(meal.id)")
            IconButton(onClick = onAddProduct , modifier = Modifier.fillMaxWidth()) {
                Row {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.NoteAdd,
                        contentDescription = stringResource(Res.string.button_back)
                    )
                    Text(stringResource(Res.string.library_button_add_product))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrarySearchBar(
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Controls expansion state of the search bar
    //var expanded by rememberSaveable { mutableStateOf(false) }
    var expanded = false

    SearchBar(
        modifier = modifier.semantics { traversalIndex = 0f },
        inputField = {
            SearchBarDefaults.InputField(
                query = textFieldState.text.toString(),
                onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                onSearch = {
                    onSearch(textFieldState.text.toString())
                    expanded = false
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text("Search") },
                trailingIcon = {
                    if (textFieldState.text.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                textFieldState.edit { replace(0, length, "") }
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Limpiar búsqueda"
                                )
                            }
                        )
                    }
                })
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
        content = {}
    )
}
