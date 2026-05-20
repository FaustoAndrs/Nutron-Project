package com.lazysyntax.nutron.presentation.ui.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysyntax.nutron.data.remote.authentication.AuthRepository
import com.lazysyntax.nutron.data.repository.SyncRepositoryImpl
import com.lazysyntax.nutron.data.remote.synchronization.SyncResult
import com.lazysyntax.nutron.presentation.ui.navigation.Navigator
import com.lazysyntax.nutron.presentation.ui.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val navigator: Navigator,
    private val syncRepository: SyncRepositoryImpl
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


    fun onLoginEvent(loginEvent: LoginEvent){
        when(loginEvent) {
            is LoginEvent.EmailChanged -> onEmailChanged(loginEvent.login)
            is LoginEvent.PasswordChanged -> onPasswordChanged(loginEvent.password)
            is LoginEvent.OnClickLogin -> login()
            is LoginEvent.OnClickSignUp -> onGoToSignUp()
            LoginEvent.OnClickSkipLogin -> onSkipLogin()
            LoginEvent.OnLoginSuccess -> onLoginSucces()
        }
    }


    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, loginSuccess = null, errorMessage = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, loginSuccess = null, errorMessage = null) }
    }

    fun resetLoginState() {
        //Actualiza el login con los datos del SessionManager si cierra sesión
        // y/o solo resetea el LoginUiSate
        _uiState.update { LoginUiState() }
    }

    fun login() {
        val validation = validator.validate(_uiState.value)
        if (!validation.error) {

            viewModelScope.launch {

                _uiState.update { it.copy(
                    isLoading = true,
                    loginSuccess = null,
                    errorMessage = null
                ) }
                val success = authRepository.login(
                    _uiState.value.email,
                    _uiState.value.password
                )
                println("Login en servidor - Éxito: $success")
                _uiState.update {
                    it.copy(
                        isLoading = false, 
                        loginSuccess = success,
                        errorMessage = if (!success) "Usuario o clave incorrectos" else null
                    )
                }

                var syncResult: SyncResult? = null
                if (success) {
                    syncResult = syncRepository.syncUserSetUp()
                }

                when (syncResult) {
                    SyncResult.Success -> {
                        // El usuario ya tiene perfil -> Ir al Home
                        navigator.navigateTo(Route.Profile)
                        println("SYNC: Usuario ya tiene perfil")
                    }
                     SyncResult.NotFound -> {
                        // Usuario nuevo (404) -> Ir a la configuración inicial (Calculator/Setup)
                        navigator.navigateTo(Route.SetUp(fromSignUp = true))
                        println("SYNC: Usuario nuevo to setup")
                    }
                    null -> {
                        println("SYNC: NULL")
                    }
                    SyncResult.Error -> {
                        // Error de conexión -> Mostrar mensaje al usuario
                        println("SYNC: Error de conexion")
                        //Mostrar un SnackBar
                    }
                }




            }

        }
    }

    fun onLoginSucces(){
        navigator.resetTo(route = Route.Profile)
    }
    fun onGoToSignUp(){
        navigator.navigateTo(route = Route.SignUp)
    }
    fun onSkipLogin(){
        navigator.resetTo(route = Route.SetUp(fromSignUp = true))
    }

}
