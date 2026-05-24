package com.lazysyntax.nutron.presentation.ui.features.targets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysyntax.nutron.data.remote.authentication.SessionManager
import com.lazysyntax.nutron.domain.repository.UserRepository
import com.lazysyntax.nutron.presentation.ui.features.setUp.SetUpUiState
import com.lazysyntax.nutron.presentation.ui.features.setUp.composables.Calculator
import com.lazysyntax.nutron.presentation.ui.features.targets.composables.Diet
import com.lazysyntax.nutron.presentation.ui.navigation.Navigator
import com.lazysyntax.nutron.presentation.ui.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
        fun fromLabel(label: String) =
            entries.find { it.label.equals(label, ignoreCase = true) } ?: STANDARD
    }
}

class TargetsViewModel(
    private val sessionManager: SessionManager,
    private val userSetupRepository: UserRepository,
    private val navigator: Navigator
) : ViewModel() {

    private val _diets = MutableStateFlow(
        DietPreset.entries.map { it.toDiet() } + sessionManager.getUserPreferences().customDiets
    )
    val diets: StateFlow<List<Diet>> = _diets.asStateFlow()

    val _uiState = MutableStateFlow(
        sessionManager.userData
            .map { it.toTargetsUiState() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = sessionManager.userData.value.toTargetsUiState()

            )
    )

    val uiState: StateFlow<TargetsUiState> = _uiState.value

    fun onTargetsEvent(targetsEvent: TargetsEvent) {
        when (targetsEvent) {
            is TargetsEvent.OnClickBack -> {
                navigator.goBack()
            }

            TargetsEvent.OnEditMealsDistribution -> {}
            is TargetsEvent.OnSelectDietType -> {
                onDietChanged(targetsEvent.diet)
            }

            is TargetsEvent.OnAddCustomDiet -> {
                addCustomDiet(targetsEvent.diet)
            }

            TargetsEvent.OnNavigateToStatistics -> navigator.navigateTo(Route.Statistics)
        }
    }

    private fun addCustomDiet(diet: Diet) {
        // 1. Guardar localmente en SessionManager para persistencia offline
        sessionManager.saveCustomDiet(diet)

        // 2. Actualizar lista en UI
        _diets.update { current ->
            if (current.none { it.name == diet.name }) current + diet else current
        }

        // 3. Seleccionar la nueva dieta (esto dispara la persistencia y sync)
        onDietChanged(diet)
    }

    fun onDietChanged(diet: Diet) {
        // 1. Persistencia local inmediata (Offline-first)
        // Esto actualiza SessionManager, lo cual a su vez actualiza el uiState vía el Flow de userData
        val currentSetup = sessionManager.userData.value
        val updatedSetup = currentSetup.copy(diet = diet.name)
        sessionManager.saveUserProfile(updatedSetup)

        // 2. Sincronización remota en segundo plano
        viewModelScope.launch {
            println("UPDATE TARGETS: Sincronizando dieta '${diet.name}' con el servidor...")
            // Usamos updatedSetup para calcular los valores finales para el servidor
            val currentState = updatedSetup.toTargetsUiState(diet)
            val success = userSetupRepository.updateUserDiet(currentState)

            if (success) {
                println("Server update success")
            } else {
                println("Server update failed - los cambios se mantienen localmente")
            }
        }
    }

    private fun SetUpUiState.toTargetsUiState(dietOverride: Diet? = null): TargetsUiState {
        val w = weight.replace(',', '.').toDoubleOrNull() ?: 0.0
        val h = height.replace(',', '.').toDoubleOrNull() ?: 0.0
        val a = age.toIntOrNull() ?: 0

        val bmiValue = Calculator.calculateBMI(w, h)
        val fatPercentage = Calculator.calculateFatPercentage(bmiValue, a, gender)
        val bmrValue = Calculator.calculateBMR(w, h, a, gender, fatPercentage, formula)
        val getValue = Calculator.calculateGET(bmrValue, activity.factor)
        val ebValue = Calculator.calculateEB(getValue, goal.factor).toInt()

        // Buscamos la dieta en la lista (incluyendo personalizadas) o usamos el preset
        val selectedDiet =
            dietOverride ?: diets.value.find { it.name.equals(this.diet, ignoreCase = true) }
            ?: DietPreset.fromLabel(this.diet).toDiet()

        return TargetsUiState(
            dailyKcal = ebValue.toString(),
            diet = selectedDiet,
            carbs = (ebValue * (selectedDiet.carbs / 100.0)).toInt(),
            fats = (ebValue * (selectedDiet.fat / 100.0)).toInt(),
            proteins = (ebValue * (selectedDiet.protein / 100.0)).toInt(),
        )
    }
}
