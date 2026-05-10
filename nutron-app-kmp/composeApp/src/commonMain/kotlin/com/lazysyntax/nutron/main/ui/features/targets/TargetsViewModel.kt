package com.lazysyntax.nutron.main.ui.features.targets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysyntax.nutron.data.repository.UserRepository
import com.lazysyntax.nutron.data.services.authentication.SessionManager
import com.lazysyntax.nutron.main.ui.features.setUp.SetUpEvent
import com.lazysyntax.nutron.main.ui.features.setUp.SetUpUiState
import com.lazysyntax.nutron.main.ui.features.setUp.SetUpUiStateValidation
import com.lazysyntax.nutron.main.ui.features.setUp.SetUpValidator
import com.lazysyntax.nutron.main.ui.features.targets.composables.Diet
import com.lazysyntax.nutron.main.ui.navigation.Navigator
import com.lazysyntax.nutron.main.ui.navigation.Route
import com.lazysyntax.nutron.main.utilities.validation.Validation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TargetsViewModel(
    private val sessionManager: SessionManager,
    private val userSetupRepository: UserRepository,
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(TargetsUiState())
    val uiState: StateFlow<TargetsUiState> = _uiState.asStateFlow()

    val diets = listOf(
        Diet("Standard", 50, 20, 30),
        Diet("Balanced", 25, 50, 25),
        Diet("Low fat", 60, 25, 15),
        Diet("High in protein", 25, 40, 35),
        Diet("Low carbs", 15, 30, 55),
        Diet("Ketogenic", 5, 30, 65),
        Diet("Custom", 50, 20, 30),
    )

    fun onTargetsEvent(targetsEvent: TargetsEvent) {
        when (targetsEvent) {
            is TargetsEvent.OnClickBack -> {navigator.goBack()}
            TargetsEvent.OnEditMealsDistribution -> {}
            is TargetsEvent.OnSelectDietType -> {
                onDietChanged(targetsEvent.diet)

            }
        }
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
                // Podrías manejar el error aquí si fuera necesario
            }
        }

    }


}
