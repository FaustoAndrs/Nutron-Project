package com.lazysyntax.nutron.presentation.ui.features.diary.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.domain.models.Food
import com.lazysyntax.nutron.domain.models.Meal
import com.lazysyntax.nutron.presentation.theme.Theme
import com.lazysyntax.nutron.presentation.ui.features.diary.DiaryEvent
import com.lazysyntax.nutron.presentation.ui.features.diary.DiaryUiState
import com.lazysyntax.nutron.presentation.ui.features.targets.TargetsUiState
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.add_check
import nutron.composeapp.generated.resources.barcode_scanner_24px
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
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.MoreVert, contentDescription = "More options"
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
) {
    val datePickerState = rememberDatePickerState()

    CenterAlignedTopAppBar(
        title = {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    IconButton(onClick = { onEvent(DiaryEvent.OnClickPreviousDay) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "Día anterior"
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.clickable { onEvent(DiaryEvent.OnClickChangeDate) }) {
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
                    IconButton(onClick = { onEvent(DiaryEvent.OnClickNextDay) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "Día siguiente"
                        )
                    }
                }
            )
        },
        actions = {
            IconButton(onClick = { onEvent(DiaryEvent.OnAddMeal(Meal(name = "PreEntreno"))) }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
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
    DatePickerDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(onClick = {
            onDateSelected()
        }) {
            Text("OK")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    }) {
        DatePicker(state = datePickerState)
    }
}


data class MacrosCount(
    val proteins: Double = 0.0,
    val carbohydrates: Double = 0.0,
    val fats: Double = 0.0,
    val calories: Double = 0.0
) {
    operator fun plus(other: MacrosCount): MacrosCount {
        return MacrosCount(
            proteins = this.proteins + other.proteins,
            carbohydrates = this.carbohydrates + other.carbohydrates,
            fats = this.fats + other.fats,
            calories = this.calories + other.calories
        )
    }
}

@Composable
fun MacroProgressBar(
    label: String,
    current: Double,
    target: Double,
    unit: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val progressValue = if (target > 0) (current / target).toFloat().coerceIn(0f, 1.1f) else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progressValue,
        animationSpec = tween(durationMillis = 1000),
        label = "progressAnimation"
    )

    Column(modifier = modifier.padding(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${current.toInt()} / ${target.toInt()} $unit",
                style = MaterialTheme.typography.labelSmall,
                color = if (current > target) MaterialTheme.colorScheme.error else Color.Unspecified
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { animatedProgress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(6.dp)),
            color = if (current > target) MaterialTheme.colorScheme.error else color,
            trackColor = color.copy(alpha = 0.2f),
            strokeCap = StrokeCap.Round,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrevMacrosbar() {
    MacroProgressBar(
        label = "kalories",
        current = 2100.0,
        target = 2490.3,
        unit = "Kcal",
        color = MaterialTheme.colorScheme.primary
    )
}

fun Meal.calculateMacros(): MacrosCount {
    return foods?.fold(MacrosCount()) { acc, food ->
        val n = food.nutriments
        acc + MacrosCount(
            proteins = n?.proteins ?: 0.0,
            carbohydrates = n?.carbs ?: 0.0,
            fats = n?.fat ?: 0.0,
            calories = n?.calories ?: 0.0
        )
    } ?: MacrosCount()
}

fun TargetsUiState.calculateTargetGrams(): MacrosCount {
    val totalKcal = dailyKcal.toDoubleOrNull() ?: 0.0
    return MacrosCount(
        proteins = (totalKcal * (diet.protein / 100.0)) / 4.0,
        carbohydrates = (totalKcal * (diet.carbs / 100.0)) / 4.0,
        fats = (totalKcal * (diet.fat / 100.0)) / 9.0,
        calories = totalKcal
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealCard(
    meal: Meal,
    modifier: Modifier = Modifier,
    onAddProduct: () -> Unit,
    onDeleteFood: (Food) -> Unit
) {
    val productCount = meal.calculateMacros()
    var showNutriments = rememberSaveable { mutableStateOf(true) }

    ElevatedCard(
        modifier = modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {   // Título de la comida (ej. Desayuno)
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    content = {
                        Text(
                            text = meal.name ?: "Sin nombre",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        IconButton(
                            modifier = Modifier.padding(0.dp),
                            onClick = { showNutriments.value = !showNutriments.value },
                            content = {
                                if (showNutriments.value) Icon(
                                    imageVector = Icons.Default.ExpandLess,
                                    contentDescription = stringResource(Res.string.button_back)
                                )
                                else Icon(
                                    imageVector = Icons.Default.ExpandMore,
                                    contentDescription = stringResource(Res.string.button_back)
                                )
                            })
                    }
                )
            }
        )
        HorizontalDivider(
            thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant
        )
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)

        ) {


            if (showNutriments.value) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    // Listado de productos
                    val foods = meal.foods
                    if (foods.isNullOrEmpty()) {
                        Text(
                            text = stringResource(Res.string.empty_meal),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        val localDensity = LocalDensity.current

                        foods.forEach { product ->

                            key(product.id) {
                                // 1. El estado actual ya no necesita lógica de confirmación intrusiva
                                val dismissState = rememberSwipeToDismissBoxState(
                                    positionalThreshold = { with(localDensity) { 30.dp.toPx() } }
                                )

                                // 2. Escuchamos el cambio de estado de forma reactiva
                                // Cuando el estado pase a ser 'EndToStart', ejecutamos el borrado
                                if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                                    LaunchedEffect(product) {
                                        onDeleteFood(product)
                                    }
                                }


                                SwipeToDismissBox(
                                    state = dismissState,
                                    enableDismissFromStartToEnd = false,
                                    backgroundContent = {
                                        val color =
                                            if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                                                Color.Red.copy(alpha = 0.8f)
                                            } else {
                                                Color.Transparent
                                            }
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(color)
                                                .padding(horizontal = 20.dp),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Eliminar",
                                                tint = Color.White
                                            )
                                        }
                                    },
                                    content = {
                                        Column(
                                            Modifier.background(
                                                color = MaterialTheme.colorScheme.surface
                                            )
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth()
                                                    .padding(vertical = 4.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                            ) {
                                                Text(
                                                    text = product.name ?: "Producto desconocido",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.weight(1f),
                                                    overflow = TextOverflow.Clip,
                                                    maxLines = 1

                                                )
                                                Text(
                                                    textAlign = TextAlign.End,
                                                    text = "Cal:${product.nutriments?.calories?.toInt() ?: "Na"}",
                                                    modifier = Modifier.weight(4f),
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    overflow = TextOverflow.Clip
                                                )

                                            }
                                            Row(
                                                modifier = Modifier.fillMaxWidth()
                                                    .padding(vertical = 4.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                            ) {
                                                Text(
                                                    text = "${product.nutriments?.quantity ?: "Na"} ${product.nutriments?.quantityUnit}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    modifier = Modifier.weight(1f),
                                                    overflow = TextOverflow.Clip
                                                )
                                                Text(
                                                    textAlign = TextAlign.End,
                                                    text = "C:${product.nutriments?.carbs ?: "Na"}  " +
                                                            "P:${product.nutriments?.proteins ?: "Na"}  " +
                                                            "F:${product.nutriments?.fat ?: "Na"}",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    modifier = Modifier.weight(4f),
                                                    overflow = TextOverflow.Clip
                                                )
                                            }
                                        }
                                    }
                                )
                                HorizontalDivider(
                                    thickness = 0.5.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }

                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(100.dp))
                    .background(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                horizontalArrangement = Arrangement.SpaceEvenly,
                content = {
                    MacroItem("Proteins", productCount.proteins.toInt(), Modifier.weight(1f))
                    MacroItem(
                        "Carbohydrates",
                        productCount.carbohydrates.toInt(),
                        Modifier.weight(1f)
                    )
                    MacroItem("Fats", productCount.fats.toInt(), Modifier.weight(1f))
                    MacroItem("Calories", productCount.calories.toInt(), Modifier.weight(1f))
                }
            )
            //Spacer( modifier = Modifier.height(16.dp))
            Button(
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                ),
                onClick = onAddProduct,
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        content = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.add_check),
                                contentDescription = stringResource(Res.string.button_back)
                            )
                            Text(stringResource(Res.string.library_button_add_product))
                        }
                    )
                })
        }

    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MacroItem(label: String, value: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(vertical = 12.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$value",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            textAlign = TextAlign.Center,
            text = label,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrarySearchBar(
    query: String,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    onScanBarcode: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onCleanQuery: () -> Unit,
    placeholder: String = ""
    ) {
    // Controls expansion state of the search bar
    //var expanded by rememberSaveable { mutableStateOf(false) }

    //Los resultados no se mostrarán en el desplegable
    var expanded = false
    Box(
        modifier.fillMaxWidth()
            .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
            .semantics { isTraversalGroup = true })
    {
        SearchBar(
            colors = SearchBarDefaults.colors().copy(
                containerColor = MaterialTheme.colorScheme.surface,

                ),
            modifier = Modifier.align(Alignment.TopCenter)
                .windowInsetsPadding(WindowInsets(0.dp))
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    modifier = Modifier.windowInsetsPadding(WindowInsets(0.dp)),
                    query = query,
                    onQueryChange = { onQueryChanged(it) },
                    onSearch = {
                        onSearch()
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text(placeholder) },
                    leadingIcon = {
                        IconButton(onClick = onSearch) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Escanear código"
                            )
                        }
                    },
                    trailingIcon = {
                        Row {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = onCleanQuery, content = {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Limpiar búsqueda"
                                    )
                                })
                            }
                            IconButton(onClick = onScanBarcode) {
                                Icon(
                                    imageVector = vectorResource(Res.drawable.barcode_scanner_24px),
                                    contentDescription = "Escanear código"
                                )
                            }
                        }
                    })
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
            content = {},
            windowInsets = WindowInsets(0.dp)
        )
    }
}

@Composable
@Preview
fun SearchBarPrev() {
    val meal = Meal(
        name = "Desayuno", foods = listOf(
            Food(
                name = "Pan",
            ), Food(
                name = "Leche",
            )
        )
    )
    Theme {
        MealCard(
            meal = meal, onAddProduct = {}, onDeleteFood = {})
    }
}