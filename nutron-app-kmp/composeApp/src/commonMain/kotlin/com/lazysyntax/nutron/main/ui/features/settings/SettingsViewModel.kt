package com.lazysyntax.nutron.main.ui.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysyntax.nutron.data.services.authentication.AuthRepository
import com.lazysyntax.nutron.data.services.authentication.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState(language = sessionManager.language.value))
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    init {
        // Mantener el estado de la UI sincronizado con el SessionManager
        viewModelScope.launch {
            sessionManager.language.collect { lang ->
                _uiState.update { it.copy(language = lang) }
            }
        }
    }

    fun onSettingsEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.LanguageChanged -> {
                sessionManager.setLanguage(event.lang)
            }

            SettingsEvent.OnClickLanguage -> {
                // Aquí podrías mostrar un diálogo, por ahora alternamos entre "es" y "en"
                val current = sessionManager.language.value
                val next = if (current == "es") "en" else "es"

                onSettingsEvent(SettingsEvent.LanguageChanged(next))

            }
        }
    }



    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onSuccess()
        }
    }
}
