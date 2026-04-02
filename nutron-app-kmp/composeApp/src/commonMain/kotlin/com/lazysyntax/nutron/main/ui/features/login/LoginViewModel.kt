package com.lazysyntax.nutron.main.ui.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysyntax.nutron.data.services.authentication.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val validator = LoginValidator()

    val validationState: StateFlow<LoginUiStateValidation> = _uiState
        .combine(MutableStateFlow(Unit)) { state, _ ->
            validator.validate(state) as LoginUiStateValidation
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LoginUiStateValidation()
        )

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, loginSuccess = null, errorMessage = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, loginSuccess = null, errorMessage = null) }
    }

    fun login() {
        val validation = validator.validate(_uiState.value)
        if (!validation.error) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, loginSuccess = null, errorMessage = null) }
                val success = authRepository.login(_uiState.value.email, _uiState.value.password)
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        loginSuccess = success,
                        errorMessage = if (!success) "Usuario o clave incorrectos" else null
                    )
                }
            }
        }
    }
}
