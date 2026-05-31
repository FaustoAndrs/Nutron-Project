package com.lazysyntax.nutron.presentation.ui.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysyntax.nutron.data.remote.authentication.SessionManager
import com.lazysyntax.nutron.data.repository.SyncRepositoryImpl
import com.lazysyntax.nutron.data.remote.synchronization.SyncResult
import com.lazysyntax.nutron.presentation.ui.features.setUp.SetUpUiState
import com.lazysyntax.nutron.presentation.ui.features.setUp.composables.Calculator
import com.lazysyntax.nutron.presentation.ui.navigation.Navigator
import com.lazysyntax.nutron.presentation.ui.navigation.Route
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.round

class ProfileViewModel(
    private val sessionManager: SessionManager,
    private val syncRepository: SyncRepositoryImpl,
    private val navigator: Navigator
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

    fun onNavigateToStatistics() {
        navigator.navigateTo(Route.Statistics)
    }

    private fun checkAndSyncProfile() {
        viewModelScope.launch {
            // Estrategia Offline-First: syncUserSetUpIfNeeded solo irá a la red si es necesario
            when (syncRepository.syncUserSetUpIfNeeded()) {
                is SyncResult.Success -> {
                    println("PROFILE: Datos verificados correctamente.")
                }
                is SyncResult.NotFound -> {
                    println("PROFILE: Usuario nuevo, redirigiendo a SetUp")
                    navigator.navigateTo(Route.SetUp(true))
                }
                else -> {
                    println("PROFILE: Usando datos locales por error de red o similar.")
                }
            }
        }
    }

    private fun SetUpUiState.toProfileUiState(): ProfileUiState {
        val w = weight.replace(',', '.').toDoubleOrNull() ?: 0.0
        val h = height.replace(',', '.').toDoubleOrNull() ?: 0.0
        val a = age.toIntOrNull() ?: 0

        val bmiValue = Calculator.calculateBMI(w, h)
        val fatPercentage = Calculator.calculateFatPercentage(bmiValue, a, gender)
        val bmrValue = Calculator.calculateBMR(w, h, a, gender, fatPercentage, formula)
        val tbwValue = Calculator.calculateTBW(w, h, a, gender)
        val getValue = Calculator.calculateGET(bmrValue, activity.factor)
        val ebValue = Calculator.calculateEB(getValue, goal.factor)

        return ProfileUiState(
            weight = weight,
            height = height,
            gender = gender,
            basalMetabolicRate = "${bmrValue.toInt()}",
            bodyMassIndex = "${round(bmiValue * 100) / 100.00}",
            bodyFatPercentage = "${fatPercentage.toInt()}",
            bodyWaterPercentage = "${tbwValue.toInt()}",
            gastoEnergeticoTotal = "${getValue.toInt()}",
            energeticBalance = "${ebValue.toInt()}"

        )
    }
}
