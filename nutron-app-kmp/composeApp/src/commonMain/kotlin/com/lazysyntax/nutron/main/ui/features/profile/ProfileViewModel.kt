package com.lazysyntax.nutron.main.ui.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysyntax.nutron.data.services.authentication.SessionManager
import com.lazysyntax.nutron.data.services.syncronitation.SyncRepository
import com.lazysyntax.nutron.data.services.syncronitation.SyncResult
import com.lazysyntax.nutron.main.ui.features.setUp.SetUpUiState
import com.lazysyntax.nutron.main.ui.features.setUp.composables.Calculator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.round

class ProfileViewModel(
    private val sessionManager: SessionManager,
    private val syncRepository: SyncRepository
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = sessionManager.userData
        .map { it.toProfileUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = sessionManager.userData.value.toProfileUiState()
        )

    init {
        checkAndSyncProfile()
    }

    private fun checkAndSyncProfile() {
        viewModelScope.launch {
            // Si no tenemos altura o peso, los datos locales están incompletos
            if (sessionManager.getCurrentUserData().height.isEmpty()) {
                println("PROFILE: Datos locales vacíos, sincronizando con servidor...")
                val result = syncRepository.syncUserSetUp()
                if (result is SyncResult.Error) {
                    println("PROFILE: Error al sincronizar datos")
                }
            }
        }
    }

    private fun SetUpUiState.toProfileUiState(): ProfileUiState {
        val w = weight.toDoubleOrNull() ?: 0.0
        val h = height.toDoubleOrNull() ?: 0.0
        val a = age.toIntOrNull() ?: 0

        val bmiValue = Calculator.calculateBMI(w, h)
        val fatPercentage = Calculator.calculateFatPercentage(bmiValue, a, gender)
        val bmrValue = Calculator.calculateBMR(w, h, a, gender, fatPercentage, formula)
        val tbwValue = Calculator.calculateTBW(w, h, a, gender)
        val getValue = Calculator.calculateGET(bmrValue, activity.factor)
        val ebValue = Calculator.calculateEB(getValue, goal.objective.toString())

        return ProfileUiState(
            weight = weight,
            height = height,
            gender = gender,
            basalMetabolicRate = "${bmrValue.toInt()}",
            bodyMassIndex = "${round(bmiValue * 100) / 100.00}",
            bodyFatPercentage = "${fatPercentage.toInt()}",
            bodyWaterPercentage = "${tbwValue.toInt()}",
            activityFactor = "$getValue",
            energeticBalance = "$ebValue"

        )
    }
}
