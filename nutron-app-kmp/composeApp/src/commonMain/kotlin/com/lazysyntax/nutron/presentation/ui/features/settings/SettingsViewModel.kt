package com.lazysyntax.nutron.presentation.ui.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysyntax.nutron.data.remote.authentication.AuthRepository
import com.lazysyntax.nutron.data.remote.authentication.SessionManager
import com.lazysyntax.nutron.data.util.TestDataGenerator
import com.lazysyntax.nutron.domain.repository.FoodRepository
import com.lazysyntax.nutron.domain.repository.MealRepository
import com.lazysyntax.nutron.presentation.ui.features.settings.SettingsEvent.*
import com.lazysyntax.nutron.presentation.ui.navigation.Navigator
import com.lazysyntax.nutron.presentation.ui.navigation.Route
import com.lazysyntax.nutron.presentation.ui.navigation.Route.Login
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.log

class SettingsViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager,
    private val testDataGenerator: TestDataGenerator,
    private val mealRepository: MealRepository,
    private val foodRepository: FoodRepository,
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsUiState(
            language = sessionManager.language.value,
            isDarkTheme = sessionManager.isDarkTheme.value,
            isGuestLogged = sessionManager.isGuestLogged.value
        )
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    init {
        // Mantener el estado de la UI sincronizado con el SessionManager
        viewModelScope.launch {
            sessionManager.language.collect { lang ->
                _uiState.update { it.copy(language = lang) }
            }
        }
        viewModelScope.launch {
            sessionManager.isDarkTheme.collect { isDark ->
                _uiState.update { it.copy(isDarkTheme = isDark) }
            }
        }
        // Observar comidas pendientes de sincronizar
        viewModelScope.launch {
            mealRepository.getUnsyncedMealsCount().collect { count ->
                _uiState.update { it.copy(unsyncedMealsCount = count) }
            }
        }
        viewModelScope.launch {
            sessionManager.isGuestLogged.collect { isGuest ->
                _uiState.update { it.copy(isGuestLogged = isGuest) }
            }
        }
    }

    fun onSettingsEvent(event: SettingsEvent) {
        when (event) {

            is LanguageChanged -> {
                sessionManager.setLanguage(event.lang)
            }

            OnClickLanguage -> {
                // Aquí podrías mostrar un diálogo, por ahora alternamos entre "es" y "en"
                val current = sessionManager.language.value
                val next = if (current == "es") "en" else "es"

                onSettingsEvent(LanguageChanged(next))
            }

            is OnToggleDarkTheme -> {
                sessionManager.setDarkTheme(event.enabled)
            }

            GenerateTestData -> {
                viewModelScope.launch {
                    _uiState.update { it.copy(isGeneratingTestData = true, showMealsCountAdvise = false) }

                    testDataGenerator.generateData(months = 2)
                    _uiState.update { it.copy(isGeneratingTestData = false, showMealsCountAdvise = true) }
                }
            }

            OnClickSync -> {
                if(_uiState.value.isGuestLogged){
                    _uiState.update { it.copy(showAdviseCreateAccount = true) }
                }else{
                    syncPendingMeals()
                }
            }

            OnClickLogOut -> {
                if (uiState.value.unsyncedMealsCount > 0) {
                    _uiState.update { it.copy(showLogoutConfirmation = true) }
                } else {
                    logout()
                }
            }

            OnConfirmLogOut -> {
                _uiState.update { it.copy(showLogoutConfirmation = false) }
                logout()
            }

            OnDismissLogOut -> {
                _uiState.update { it.copy(showLogoutConfirmation = false) }
            }
            OnConfirmAdviseCreateAccount -> {
                _uiState.update { it.copy(showAdviseCreateAccount = false) }
            }
            OnCreateAccount -> {

                navigator.navigateTo(Route.SignUp)
            }
        }
    }

    private fun syncPendingMeals() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true) }
            val count = mealRepository.syncPendingMeals()
            val countFoods = foodRepository.syncPendingFoods()
            println("SETTINGS: Sincronizadas $count comidas.")
            println("SETTINGS: Sincronizadas $countFoods foods.")
            _uiState.update { it.copy(isSyncing = false) }
        }
    }



    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            navigator.resetTo(Login)
        }
    }
}
