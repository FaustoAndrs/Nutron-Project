package com.lazysyntax.nutron.presentation.ui.features.login.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysyntax.nutron.data.remote.authentication.AuthRepository
import com.lazysyntax.nutron.data.remote.authentication.AuthResult
import com.lazysyntax.nutron.data.remote.authentication.SessionManager
import com.lazysyntax.nutron.data.remote.synchronization.toUserSetupDto
import com.lazysyntax.nutron.presentation.ui.navigation.Navigator
import com.lazysyntax.nutron.presentation.ui.navigation.Route
import com.lazysyntax.nutron.domain.models.User
import com.lazysyntax.nutron.presentation.utilities.validation.Validation
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
    private val navigator: Navigator,
    private  val sessionManager: SessionManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val _dirtyFields = MutableStateFlow<Set<String>>(emptySet())
    private val validator = SingUpValidator()

    val validationState: StateFlow<SignUpUiStateValidation> =
        combine(_uiState, _dirtyFields) { state, dirty ->
            val actualValidation = validator.validate(state) as SignUpUiStateValidation

            // Enmascaramos los errores si el campo no es "dirty"
            actualValidation.copy(
              userNameValidation  = if ("userName" in dirty) actualValidation.userNameValidation else object :
                    Validation {},
                fullNameValidation = if ("fullName" in dirty) actualValidation.fullNameValidation else object :
                    Validation {},
                emailValidation = if ("email" in dirty) actualValidation.emailValidation else object :
                    Validation {},
                passwordValidation = if ("password" in dirty) actualValidation.passwordValidation else object :
                    Validation {},
            )
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
            SignUpEvent.OnClickSignUp -> {
                if(!sessionManager.isGuestLogged.value) {
                    onSignUp(uiState.value.toNewUserEntity()) //<- Se genera un Uuid
                }else{
                    onSignUpGuest() //<- Se recupera el Uuid del usuario Invitado para vincular el invitado con un nuevo usuario.
                }
            }
            SignUpEvent.OnSignUpSuccess -> onSignUpSuccess()
            SignUpEvent.OnBack -> onBack()
        }
    }
    private fun markDirty(field: String) {
        _dirtyFields.update { it + field }
    }

    fun onUserNameChanged(userName: String) {
        _uiState.update { it.copy(userName = userName, signUpSuccess = null, errorMessage = null) }
        markDirty("userName")
    }

    fun onFullNameChanged(fullName: String) {
        _uiState.update { it.copy(fullName = fullName, signUpSuccess = null, errorMessage = null) }
        markDirty("fullName")
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, signUpSuccess = null, errorMessage = null) }
        markDirty("email")
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, signUpSuccess = null, errorMessage = null) }
        markDirty("password")
    }


    fun onSignUp(newUserDto: User) {
        val validation = validator.validate(_uiState.value)
        if (!validation.error) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val result = authRepository.register(newUser = newUserDto)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        signUpSuccess = result is AuthResult.Success,
                        errorMessage = when (result) {
                            AuthResult.Success -> null
                            AuthResult.UserAlreadyExists -> "El correo electrónico o nombre de usuario ya está registrado."
                            AuthResult.InvalidCredentials -> "Error al crear la cuenta. Los datos pueden ser inválidos."
                            AuthResult.NetworkError -> "Error de conexión. Revisa el servidor o tu internet."
                            is AuthResult.UnknownError -> result.message ?: "Error desconocido"
                        }
                    )
                }

                if (result is AuthResult.Success) {
                    // LLAMAMOS AL ÉXITO AQUÍ, cuando ya tenemos la respuesta
                    onSignUpSuccess()
                }
            }
        }
    }

    fun onSignUpGuest(){
        var currentGuestId = sessionManager.getUserId()
        var guestState = uiState.value
        var preferences = sessionManager.getUserPreferences()

        if(currentGuestId != null){
            var newUser = User(
                id = currentGuestId,
                userName = guestState.userName,
                fullName = guestState.fullName,
                email = guestState.email,
                password = guestState.password,
                userSetup = preferences.setupUiState.toUserSetupDto()
            )

            sessionManager.setGuestLogin(logged = false)
            onSignUp(newUserDto = newUser)
        }else{
            println("DEBUG SIGN UP (as GUEST): currentGuestId -> $currentGuestId is NULL, no data synced")
        }

    }

    fun onBack() {
        navigator.goBack()
    }

    fun onSignUpSuccess() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // ESPERAMOS a que el login termine
            val loginResult = authRepository.login(uiState.value.email, uiState.value.password)

            _uiState.update { it.copy(isLoading = false) }

            if (loginResult is AuthResult.Success) {
                // Navegamos solo después de confirmar que la sesión se guardó
                navigator.resetTo(route = Route.SetUp(fromSignUp = true))
            } else {
                _uiState.update {
                    it.copy(
                        errorMessage = when (loginResult) {
                            AuthResult.NetworkError -> "Error de conexión al iniciar sesión."
                            else -> "Cuenta creada, pero hubo un error al iniciar sesión automáticamente."
                        }
                    )
                }
            }
        }
    }
}
