package com.lazysyntax.nutron.presentation.ui.features.setUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysyntax.nutron.domain.repository.UserRepository
import com.lazysyntax.nutron.data.remote.authentication.SessionManager
import com.lazysyntax.nutron.presentation.ui.navigation.Navigator
import com.lazysyntax.nutron.presentation.ui.navigation.Route
import com.lazysyntax.nutron.presentation.utilities.validation.Validation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SetUpViewModel(
    private val sessionManager: SessionManager,
    private val userSetupRepository: UserRepository,
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(sessionManager.getCurrentUserData())
    val uiState: StateFlow<SetUpUiState> = _uiState.asStateFlow()

    // 1. Añadimos un Set para rastrear los campos interactuados
    private val _dirtyFields = MutableStateFlow<Set<String>>(emptySet())


    init {
        // Observamos los cambios en el SessionManager
        viewModelScope.launch {
            sessionManager.userData.collect { freshData ->
                // Solo actualizamos si el estado actual está vacío (o según tu lógica)
                // para no sobreescribir lo que el usuario esté escribiendo
                if (freshData == SetUpUiState() || _uiState.value == SetUpUiState() || _dirtyFields.value.isEmpty()) {
                    _uiState.value = freshData
                    // Si estamos recibiendo un estado vacío (logout), reseteamos también los campos interactuados
                    if (freshData == SetUpUiState()) {
                        _dirtyFields.value = emptySet()
                    }
                }
            }
        }
    }

    private val validator = SetUpValidator()

    // 2. Combinamos el uiState con los campos dirty para filtrar la validación
    val validationState: StateFlow<SetUpUiStateValidation> =
        combine(_uiState, _dirtyFields) { state, dirty ->
            val actualValidation = validator.validate(state) as SetUpUiStateValidation

            // Enmascaramos los errores si el campo no es "dirty"
            actualValidation.copy(
                weightValidation = if ("weight" in dirty) actualValidation.weightValidation else object :
                    Validation {},
                heightValidation = if ("height" in dirty) actualValidation.heightValidation else object :
                    Validation {},
                ageValidation = if ("age" in dirty) actualValidation.ageValidation else object :
                    Validation {},
                genderValidation = if ("gender" in dirty) actualValidation.genderValidation else object :
                    Validation {},
                activityValidation = if ("activity" in dirty) actualValidation.activityValidation else object :
                    Validation {},
                goalValidation = if ("goal" in dirty) actualValidation.goalValidation else object :
                    Validation {},
                formulaValidation = if ("formula" in dirty) actualValidation.formulaValidation else object :
                    Validation {},
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SetUpUiStateValidation()
        )


    fun onSetUpEvent(setUpEvent: SetUpEvent) {
        when (setUpEvent) {
            is SetUpEvent.WeightChanged -> {
                onWeightChanged(setUpEvent.weight)
            }
            is SetUpEvent.HeightChanged -> onHeightChanged(setUpEvent.height)
            is SetUpEvent.GenderChanged -> onGenderChanged(setUpEvent.gender)
            is SetUpEvent.AgeChanged -> onAgeChanged(setUpEvent.age)
            is SetUpEvent.ActivityChanged -> onActivityChanged(setUpEvent.activity)
            is SetUpEvent.GoalChanged -> onGoalChanged(setUpEvent.goal)
            is SetUpEvent.FormulaChanged -> onFormulaChanged(setUpEvent.formula)
            SetUpEvent.OnClickSave -> onSave()
            is SetUpEvent.OnClickBack -> onBack(setUpEvent.fromSignUp)
        }
    }

    // 3. Función auxiliar para marcar un campo como interactuado
    private fun markDirty(field: String) {
        _dirtyFields.update { it + field }
    }
    fun onWeightChanged(weight: String) {
            _uiState.update { it.copy(weight = weight) }
            markDirty("weight")
    }

    fun onHeightChanged(height: String) {
        _uiState.update { it.copy(height = height) }
        markDirty("height")
    }

    fun onGenderChanged(gender: String) {
        _uiState.update { it.copy(gender = gender) }
        markDirty("gender")
    }

    fun onAgeChanged(age: String) {
        _uiState.update { it.copy(age = age) }
        markDirty("age")
    }

    fun onActivityChanged(activity: Activity) {
        _uiState.update { it.copy(activity = activity) }
        markDirty("activity")
    }

    fun onGoalChanged(goal: Goal) {
        _uiState.update { it.copy(goal = goal) }
        markDirty("goal")
    }

    fun onFormulaChanged(formula: String) {
        _uiState.update { it.copy(formula = formula) }
        markDirty("formula")
    }

    fun onSave() {
        val currentState = uiState.value
        val totalValidation = validator.validate(currentState)
        if(totalValidation.error)
        {
            _dirtyFields.update { it + "weight" + "height" + "age" + "gender" + "activity" + "goal" + "formula" }
            return
        }

        viewModelScope.launch {
            sessionManager.saveUserProfile(currentState)
        }
        println("ON Save: sManager...: ${sessionManager.authSession.value?.accessToken}")

        viewModelScope.launch {
            println("ON Save: IsLoggedIn...: ${sessionManager.authSession.value != null}")
            if (sessionManager.authSession.value != null) {
                println("ON Save: Intentando actualizar en servidor...")
                val success = userSetupRepository.updateUserSetup(currentState)
                if (success) {
                    println("Server update success")
                } else {
                    println("Server update failed")
                    // Podrías manejar el error aquí si fuera necesario
                }
            }
            navigator.resetTo(route = Route.Profile)
        }
    }

    fun onBack(fromSignUp: Boolean) {
        if (fromSignUp) {
            navigator.resetTo(route = Route.Login)
        } else {
            navigator.goBack()
        }

    }




}
