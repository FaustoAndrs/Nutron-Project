package com.lazysyntax.nutron.presentation.ui.features.targets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysyntax.nutron.domain.repository.UserRepository
import com.lazysyntax.nutron.data.remote.authentication.SessionManager
import com.lazysyntax.nutron.presentation.ui.features.setUp.SetUpUiState
import com.lazysyntax.nutron.presentation.ui.features.setUp.composables.Calculator
import com.lazysyntax.nutron.presentation.ui.features.targets.composables.Diet
import com.lazysyntax.nutron.presentation.ui.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class DietPreset(val label: String, val carbs: Int, val protein: Int, val fat: Int) {
    STANDARD("Standard", 50, 20, 30),
    BALANCED("Balanced", 25, 50, 25),
    LOW_FAT("Low fat", 60, 25, 15),
    HIGH_IN_PROTEIN("High in protein", 25, 40, 35),
    LOW_CARBS("Low carbs", 15, 30, 55),
    KETOGENIC("Ketogenic", 5, 30, 65);

    fun toDiet() = Diet(label, carbs, protein, fat)

    companion object {
        fun fromLabel(label: String) = entries.find { it.label == label } ?: STANDARD
    }
}

class TargetsViewModel(
    private val sessionManager: SessionManager,
    private val userSetupRepository: UserRepository,
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        sessionManager.getCurrentUserData().toTargetsUiState()
    )
    val uiState: StateFlow<TargetsUiState> = _uiState.asStateFlow()

    private val _diets = MutableStateFlow(DietPreset.entries.map { it.toDiet() })
    val diets: StateFlow<List<Diet>> = _diets.asStateFlow()

    fun onTargetsEvent(targetsEvent: TargetsEvent) {
        when (targetsEvent) {
            is TargetsEvent.OnClickBack -> { navigator.goBack() }
            TargetsEvent.OnEditMealsDistribution -> {}
            is TargetsEvent.OnSelectDietType -> {
                onDietChanged(targetsEvent.diet)
            }
            is TargetsEvent.OnAddCustomDiet -> {
                addCustomDiet(targetsEvent.diet)
            }
        }
    }

    private fun addCustomDiet(diet: Diet) {
        _diets.update { it + diet }
        // Opcionalmente, seleccionar la nueva dieta automáticamente
        onDietChanged(diet)
    }

    fun onDietChanged(diet: Diet) {
        _uiState.update { it.copy(diet = diet) }

        viewModelScope.launch {
            println("UPDATE TARGETS: Intentando actualizar en servidor...")
            val success = userSetupRepository.updateUserDiet(uiState.value)
            if (success) {
                println("Server update success")
            } else {
                println("Server update failed")
            }
        }
    }

    private fun SetUpUiState.toTargetsUiState(): TargetsUiState {
        val w = weight.toDoubleOrNull() ?: 0.0
        val h = height.toDoubleOrNull() ?: 0.0
        val a = age.toIntOrNull() ?: 0

        val bmiValue = Calculator.calculateBMI(w, h)
        val fatPercentage = Calculator.calculateFatPercentage(bmiValue, a, gender)
        val bmrValue = Calculator.calculateBMR(w, h, a, gender, fatPercentage, formula)
        val getValue = Calculator.calculateGET(bmrValue, activity.factor)
        val ebValue = Calculator.calculateEB(getValue, goal.objective.toString())

        val currentDietLabel = this.diet
        val initialDiet = DietPreset.fromLabel(currentDietLabel).toDiet()

        return TargetsUiState(
            dailyKcal = "${ebValue.toInt()}",
            diet = initialDiet
        )
    }
}
