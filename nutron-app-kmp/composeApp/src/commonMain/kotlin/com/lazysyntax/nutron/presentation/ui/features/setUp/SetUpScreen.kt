package com.lazysyntax.nutron.presentation.ui.features.setUp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.presentation.ui.features.setUp.composables.ActivitySelector
import com.lazysyntax.nutron.presentation.ui.features.setUp.composables.BMRSelector
import com.lazysyntax.nutron.presentation.ui.features.setUp.composables.GoalSelector
import com.lazysyntax.nutron.presentation.ui.navigation.composables.TopAppBarWithBack
import com.lazysyntax.nutron.presentation.utilities.validation.Validation
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.setup_age_label
import nutron.composeapp.generated.resources.setup_button_save
import nutron.composeapp.generated.resources.setup_desc_guest
import nutron.composeapp.generated.resources.setup_desc_user
import nutron.composeapp.generated.resources.setup_welcome_guest
import nutron.composeapp.generated.resources.setup_welcome_user
import nutron.composeapp.generated.resources.title_setup
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(
    fromSignUp: Boolean,
    viewModel: SetUpViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val validationState by viewModel.validationState.collectAsState()

    SetupContent(
        fromSignUp = fromSignUp,
        uiState = uiState,
        validationState = validationState,
        onWeightChanged = { viewModel.onSetUpEvent(SetUpEvent.WeightChanged(it)) },
        onHeightChanged = { viewModel.onSetUpEvent(SetUpEvent.HeightChanged(it)) },
        onGenderChanged = { viewModel.onSetUpEvent(SetUpEvent.GenderChanged(it)) },
        onAgeChanged = { viewModel.onSetUpEvent(SetUpEvent.AgeChanged(it)) },
        onActivityChanged = { viewModel.onSetUpEvent(SetUpEvent.ActivityChanged(it)) },
        onGoalChanged = { viewModel.onSetUpEvent(SetUpEvent.GoalChanged(it)) },
        onFormulaChanged = { viewModel.onSetUpEvent(SetUpEvent.FormulaChanged(it)) },
        onClickSave = { viewModel.onSetUpEvent(SetUpEvent.OnClickSave) },
        onClickBack = { viewModel.onSetUpEvent(SetUpEvent.OnClickBack(fromSignUp)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupContent(
    fromSignUp: Boolean,
    uiState: SetUpUiState,
    validationState: SetUpUiStateValidation,
    onWeightChanged: (String) -> Unit,
    onHeightChanged: (String) -> Unit,
    onGenderChanged: (Gender) -> Unit,
    onAgeChanged: (String) -> Unit,
    onActivityChanged: (Activity) -> Unit,
    onGoalChanged: (Goal) -> Unit,
    onFormulaChanged: (String) -> Unit,
    onClickSave: () -> Unit,
    onClickBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var showActivitySheet by remember { mutableStateOf(false) }
    var showGoalSheet by remember { mutableStateOf(false) }
    var showFormulaSheet by remember { mutableStateOf(false) }

    val activitySheetState = rememberModalBottomSheetState()
    val goalSheetState = rememberModalBottomSheetState()
    val formulaSheetState = rememberModalBottomSheetState()

    val focusManager = LocalFocusManager.current
    Scaffold(
        topBar = {
            TopAppBarWithBack(
                title = stringResource(Res.string.title_setup),
                onNavigationClick = onClickBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                }
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(24.dp), // Margen general de formulario
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Cabecera del Formulario
            HeaderSection(
                title = if (fromSignUp) stringResource(Res.string.setup_welcome_user) else stringResource(
                    Res.string.setup_welcome_guest
                ),
                description = if (fromSignUp) stringResource(Res.string.setup_desc_user) else stringResource(
                    Res.string.setup_desc_guest
                )
            )

            // Grupo 1: Perfil Físico
            FormSectionTitle("Tu Perfil Físico")

            FormField(
                label = "Peso actual",
                error = validationState.weightValidation.error,
                errorMessage = validationState.weightValidation.errorMessage
            ) {
                OutlinedTextField(
                    value = uiState.weight,
                    onValueChange = onWeightChanged,
                    placeholder = { Text("Ej: 75.5", color = MaterialTheme.colorScheme.secondary) },
                    suffix = { Text("kg") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(12.dp),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true
                )
            }

            FormField(
                label = "Altura",
                error = validationState.heightValidation.error,
                errorMessage = validationState.heightValidation.errorMessage
            ) {
                OutlinedTextField(
                    value = uiState.height,
                    onValueChange = onHeightChanged,
                    placeholder = {
                        Text(
                            "Ej: 180",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    suffix = { Text("cm") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(12.dp),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true
                )
            }

            FormField(
                label = "Edad",
                error = validationState.ageValidation.error,
                errorMessage = validationState.ageValidation.errorMessage
            ) {
                OutlinedTextField(
                    value = uiState.age,
                    onValueChange = onAgeChanged,
                    placeholder = {
                        Text(
                            "Ej: 25",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    suffix = { Text(stringResource(Res.string.setup_age_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    shape = RoundedCornerShape(12.dp),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    singleLine = true
                )
            }

            FormField(label = "Género") {
                GenderSelectorSegmentedButton(
                    uiState = uiState.gender.name,
                    onGenderChanged = onGenderChanged,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp)

            // Grupo 2: Objetivos
            FormSectionTitle("Objetivos")

            FormField(label = "Nivel de actividad diaria") {
                SelectableField(
                    value = stringResource(uiState.activity.level),
                    onClick = { showActivitySheet = true },
                    icon = Icons.Default.Pool
                )
            }

            FormField(label = "Tu meta principal") {
                SelectableField(
                    value = stringResource(uiState.goal.objective),
                    onClick = { showGoalSheet = true },
                    icon = Icons.Default.Flag
                )
            }

            FormField(label = "Método de cálculo (BMR)") {
                SelectableField(
                    value = uiState.formula,
                    onClick = { showFormulaSheet = true },
                    icon = Icons.Default.Calculate
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onClickSave,
                enabled = !validationState.error,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    stringResource(Res.string.setup_button_save),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Selectores (BottomSheets)
        if (showActivitySheet) {
            ActivitySelector(
                activitySheetState,
                scope,
                onActivityChanged,
                { showActivitySheet = false })
        }
        if (showGoalSheet) {
            GoalSelector(goalSheetState, scope, onGoalChanged, { showGoalSheet = false })
        }
        if (showFormulaSheet) {
            BMRSelector(formulaSheetState, scope, onFormulaChanged, { showFormulaSheet = false })
        }
    }
}


@Composable
fun HeaderSection(title: String, description: String) {
    Column {
        Text(title, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Text(
            description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun FormSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun FormField(
    label: String,
    error: Boolean = false,
    errorMessage: StringResource? = null,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        content()
        if (error && errorMessage != null) {
            Text(
                text = stringResource(errorMessage),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

@Composable
fun SelectableField(value: String, icon: ImageVector, onClick: () -> Unit) {
    val focusManager = LocalFocusManager.current
    Surface(
        onClick = {
            focusManager.clearFocus()
            onClick(
            )
        },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                icon,
                null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Text(value, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                null,
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun FormInputRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    suffix: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    validation: Validation = object : Validation {}
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        placeholder,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                suffix = suffix?.let { { Text(it) } },
                modifier = Modifier.width(140.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.End),
                singleLine = true,
                keyboardOptions = keyboardOptions,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                )
            )
        }
        if (validation.error) {
            Text(
                text = stringResource(validation.errorMessage!!),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
fun FormSelectableRow(
    label: String,
    value: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    ListItem(
        headlineContent = { Text(label) },
        supportingContent = {
            Text(
                value,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        },
        leadingContent = { Icon(icon, null, tint = MaterialTheme.colorScheme.primary) },
        trailingContent = {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                null,
                tint = MaterialTheme.colorScheme.outline
            )
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier.clickable {
            focusManager.clearFocus()
            onClick()
        }
    )
}

@Composable
fun SetupSection(
    title: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        title?.let {
            Text(
                text = it.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                fontWeight = FontWeight.SemiBold
            )
        }
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(content = content)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderSelectorSegmentedButton(
    modifier: Modifier = Modifier,
    uiState: String,
    onGenderChanged: (Gender) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val options = Gender.entries.filter { g -> g != Gender.NON }
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                onClick = {
                    focusManager.clearFocus()
                    onGenderChanged(label)
                },
                selected = label.name == uiState,
                label = { Text(label.name) },
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    activeContentColor = MaterialTheme.colorScheme.secondary,
                    activeBorderColor = MaterialTheme.colorScheme.primary,
                    inactiveContainerColor = MaterialTheme.colorScheme.surface,
                    inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    inactiveBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun prevSelector() {
    GenderSelectorSegmentedButton(uiState = "", onGenderChanged = {})
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SetupScreenPreview() {
    SetupContent(
        fromSignUp = false,
        uiState = SetUpUiState(),
        validationState = SetUpUiStateValidation(),
        onWeightChanged = {},
        onHeightChanged = {},
        onGenderChanged = {},
        onAgeChanged = {},
        onActivityChanged = {},
        onGoalChanged = {},
        onFormulaChanged = {},
        onClickSave = {},
        onClickBack = {}
    )
}
