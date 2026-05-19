package com.lazysyntax.nutron.presentation.ui.features.login.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysyntax.nutron.data.remote.authentication.AuthRepository
import com.lazysyntax.nutron.presentation.ui.navigation.Navigator
import com.lazysyntax.nutron.presentation.ui.navigation.Route
import com.lazysyntax.nutron.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authRepository: AuthRepository,
    private val navigator: Navigator
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


    fun onSignUpEvent(signUpEvent: SignUpEvent) {
        when (signUpEvent) {
            is SignUpEvent.UserNameChanged -> onUserNameChanged(signUpEvent.userName)
            is SignUpEvent.FullNameChanged -> onFullNameChanged(signUpEvent.fullName)
            is SignUpEvent.EmailChanged -> onEmailChanged(signUpEvent.email)
            is SignUpEvent.PasswordChanged -> onPasswordChanged(signUpEvent.password)
            SignUpEvent.OnClickSignUp -> onSignUp(uiState.value.toNewUserEntity())
            SignUpEvent.OnSignUpSuccess -> onSignUpSuccess()
            SignUpEvent.OnBack -> onBack()
        }
    }

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


    fun onSignUp(newUser: User) {
        val validation = validator.validate(_uiState.value)
        if (!validation.error) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val success = authRepository.register(newUser = newUser)

                _uiState.update {
                    it.copy(isLoading = false, signUpSuccess = success)
                }

                if (success) {
                    // LLAMAMOS AL ÉXITO AQUÍ, cuando ya tenemos la respuesta
                    onSignUpSuccess()
                } else {
                    _uiState.update { it.copy(errorMessage = "Error al crear la cuenta. Intente de nuevo.") }
                }
            }
        }
    }

    fun onBack() {
        navigator.goBack()
    }

    fun onSignUpSuccess() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // ESPERAMOS a que el login termine
            val loginSuccess = authRepository.login(uiState.value.email, uiState.value.password)

            _uiState.update { it.copy(isLoading = false) }

            if (loginSuccess) {
                // Navegamos solo después de confirmar que la sesión se guardó
                navigator.resetTo(route = Route.SetUp(fromSignUp = true))
            } else {
                _uiState.update { it.copy(errorMessage = "Cuenta creada, pero hubo un error al iniciar sesión automáticamente.") }
            }
        }
    }
}
