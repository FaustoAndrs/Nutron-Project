package com.lazysyntax.nutron.main.ui.features.login.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysyntax.nutron.data.services.authentication.AuthRepository
import com.lazysyntax.nutron.main.ui.features.login.LoginValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val validator = SingUpValidator()

    val validationState: StateFlow<SignUpUiStateValidation> = _uiState
        .combine(MutableStateFlow(Unit)) { state, _ ->
            validator.validate(state) as SignUpUiStateValidation
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SignUpUiStateValidation()
        )
    fun onUserNameChanged(userName: String) {
        _uiState.update { it.copy(userName = userName, signUpSuccess = null, errorMessage = null) }
    }
    fun onFullNameChanged(fullName: String) {
        _uiState.update { it.copy(fullName = fullName, signUpSuccess = null, errorMessage = null) }
    }
    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, signUpSuccess = null, errorMessage = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, signUpSuccess = null, errorMessage = null) }
    }

    fun signUp() {
        val validation = validator.validate(_uiState.value)
        if (!validation.error) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, signUpSuccess = null, errorMessage = null) }
                val success = authRepository.register(
                    _uiState.value.userName,
                    _uiState.value.fullName,
                    _uiState.value.email,
                    _uiState.value.password
                )
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        signUpSuccess = success,
                        errorMessage = if (!success) "Valores incorrectos o campos vacíos." else null
                    )
                }
            }
        }
    }
}
