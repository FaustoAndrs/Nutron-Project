package com.lazysyntax.nutron.main.ui.features.setUp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.main.ui.composables.TextFieldWithErrorState
import com.lazysyntax.nutron.main.ui.features.setUp.composables.ActivitySelector
import com.lazysyntax.nutron.main.ui.features.setUp.composables.GoalSelector
import com.lazysyntax.nutron.main.ui.features.setUp.composables.MetabolicFormula
import com.lazysyntax.nutron.main.ui.navigation.composables.TopAppBarWhitBackButtonCommon
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.setup_button_save
import nutron.composeapp.generated.resources.setup_desc_guest
import nutron.composeapp.generated.resources.setup_desc_user
import nutron.composeapp.generated.resources.setup_welcome_guest
import nutron.composeapp.generated.resources.setup_welcome_user
import nutron.composeapp.generated.resources.title_setup
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
        onWeightChanged = { viewModel.onSetUpEvent(SetUpEvent.WeightChanged(it))},
        onHeightChanged = { viewModel.onSetUpEvent(SetUpEvent.HeightChanged(it)) },
        onGenderChanged = { viewModel.onSetUpEvent(SetUpEvent.GenderChanged(it)) },
        onAgeChanged = { viewModel.onSetUpEvent(SetUpEvent.AgeChanged(it)) },
        onActivityChanged = { viewModel.onSetUpEvent(SetUpEvent.ActivityChanged(it) )},
        onGoalChanged = { viewModel.onSetUpEvent(SetUpEvent.GoalChanged(it)) },
        onFormulaChanged = { viewModel.onSetUpEvent(SetUpEvent.FormulaChanged(it))},
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
    onGenderChanged: (String) -> Unit,
    onAgeChanged: (String) -> Unit,
    onActivityChanged: (Activity) -> Unit,
    onGoalChanged: (Goal) -> Unit,
    onFormulaChanged: (String) -> Unit,
    onClickSave: () -> Unit,
    onClickBack: () -> Unit,
){
    val scope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    var showGenderSheet by remember { mutableStateOf(false) }
    var showActivitySheet by remember { mutableStateOf(false) }
    var showGoalSheet by remember { mutableStateOf(false) }
    var showFormulaSheet by remember { mutableStateOf(false) }

    val genderSheetState = rememberModalBottomSheetState()
    val activitySheetState = rememberModalBottomSheetState()
    val goalSheetState = rememberModalBottomSheetState()
    val formulaSheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            TopAppBarWhitBackButtonCommon(
                title = stringResource(Res.string.title_setup),
                onBack = onClickBack
            )
        },
    )
    { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            val title =
                if (fromSignUp) stringResource(Res.string.setup_welcome_user) else stringResource(
                    Res.string.setup_welcome_guest
                )

            val description =
                if (fromSignUp) stringResource(Res.string.setup_desc_user) else stringResource(Res.string.setup_desc_guest)

            Text(title, style = MaterialTheme.typography.headlineMedium)
            Text(description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))


            TextFieldWithErrorState(
                textoPista = "Weight",
                textoState = uiState.weight,
                validacionState = validationState.weightValidation,
                onValueChange = onWeightChanged,
                suffix = {Text("kg")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            TextFieldWithErrorState(
                textoPista = "Height",
                textoState = uiState.height,
                validacionState = validationState.heightValidation,
                onValueChange =onHeightChanged,
                suffix = {Text("cm")},
                        keyboardOptions =KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            TextFieldWithErrorState(
                textoPista = "Age",
                textoState = uiState.age,
                validacionState = validationState.ageValidation,
                onValueChange = onAgeChanged,
                keyboardOptions =KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Text(text = "Gender", style = MaterialTheme.typography.headlineSmall)
            /*TextField(
                label = { Text(text = "Gender") },
                value = uiState.gender,
                onValueChange = onGenderChanged,
                readOnly = true,
                enabled = false,
                modifier = Modifier.clickable { showGenderSheet = true })*/
            GenderSelectorSegmentedButton(uiState = uiState.gender, onGenderChanged = onGenderChanged)
            Spacer(modifier = Modifier.size(16.dp))


            TextField(
                label = { Text(text = "Activity") },
                value = stringResource(uiState.activity.level),
                onValueChange = { },
                readOnly = true,
                enabled = false,
                modifier = Modifier.clickable { showActivitySheet = true })
            Spacer(modifier = Modifier.size(8.dp))

            TextField(
                label = { Text(text = "Goal") },
                value = stringResource(uiState.goal.objective),
                onValueChange = { },
                readOnly = true,
                enabled = false,
                modifier = Modifier.clickable { showGoalSheet = true })
            Spacer(modifier = Modifier.size(16.dp))

            Text(text = "Formula", style = MaterialTheme.typography.headlineSmall)

            FormulaChipSelector(
                selectedFormula = uiState.formula,
                onFormulaSelected = onFormulaChanged
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onClickSave,
                enabled = !validationState.error,
                content = { Text(stringResource(Res.string.setup_button_save)) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }


        if (showActivitySheet) {
            ActivitySelector(
                sheetState = activitySheetState,
                scope = scope,
                onLevelSelected = onActivityChanged,
                onDismiss = { showActivitySheet = false })
        }

        if (showGoalSheet) {
            GoalSelector(
                sheetState = goalSheetState,
                scope = scope,
                onGoalSelected = onGoalChanged,
                onDismiss = { showGoalSheet = false })
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, )
@Composable
fun SetupScreenPreview() {
  var validationState = SetUpUiStateValidation()
    var uiState = SetUpUiState()
    SetupContent(
        fromSignUp = false,
        uiState = uiState,
        validationState = validationState,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderSelectorSegmentedButton(
    modifier: Modifier = Modifier,
    uiState: String,
    onGenderChanged: (String) -> Unit
) {
    val options = listOf("Hombre", "Mujer")

    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { onGenderChanged(label) },
                selected = label == uiState,
                label = { Text(label) }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FormulaChipSelector(
    modifier: Modifier = Modifier,
    selectedFormula: String,
    onFormulaSelected: (String) -> Unit
) {


    FlowRow(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        //verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MetabolicFormula.entries.forEach { equation ->
            val isSelected = selectedFormula == equation.label
            FilterChip(
                onClick = { onFormulaSelected(equation.label) },
                label = { Text(equation.label) },
                selected = isSelected,
                leadingIcon = if (isSelected) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Selected",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else null,
            )
        }
    }
}
