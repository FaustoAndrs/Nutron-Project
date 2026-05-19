package com.lazysyntax.nutron.data.remote.authentication

import com.lazysyntax.nutron.presentation.ui.features.setUp.SetUpUiState
import com.lazysyntax.nutron.domain.models.Meal
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.Settings
import com.russhwolf.settings.serialization.decodeValue
import com.russhwolf.settings.serialization.encodeValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.nullable

@OptIn(ExperimentalSettingsApi::class, ExperimentalSerializationApi::class)
class SessionManager(
    private val encryptedSettings: Settings,
    private val commonSettings: Settings
) {

    companion object {
        private const val AUTH_SESSION_KEY = "auth_session"
        private const val USER_PREFERENCES_KEY = "user_preferences"
    }

    private val authSessionSerializer = AuthSession.serializer().nullable
    private val userPreferencesSerializer = UserPreferences.serializer()

    // Cache local de las preferencias para evitar múltiples lecturas/decodificaciones al inicio
    private val initialPrefs = getUserPreferences()

    private val _authSession = MutableStateFlow(getAuthSession())
    val authSession: StateFlow<AuthSession?> = _authSession.asStateFlow()

    private val _userData = MutableStateFlow(initialPrefs.setupUiState)
    val userData: StateFlow<SetUpUiState> = _userData.asStateFlow()

    private val _language = MutableStateFlow(initialPrefs.language)
    val language: StateFlow<String> = _language.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(initialPrefs.isDarkTheme)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _mealTemplate = MutableStateFlow(initialPrefs.mealTemplate)
    val mealTemplate: StateFlow<List<Meal>> = _mealTemplate.asStateFlow()

    fun getAuthSession(): AuthSession? {
        return try {
            encryptedSettings.decodeValue(
                authSessionSerializer,
                AUTH_SESSION_KEY,
                null
            )
        } catch (e: Exception) {
            println("SESSION ERROR: Error decoding auth session: ${e.message}")
            null
        }
    }

    fun getUserPreferences(): UserPreferences {
        return try {
            commonSettings.decodeValue(
                userPreferencesSerializer,
                USER_PREFERENCES_KEY,
                UserPreferences()
            )
        } catch (e: Exception) {
            println("SESSION ERROR: Error decoding user preferences: ${e.message}")
            UserPreferences()
        }
    }

    fun saveSession(userId: String, email: String, accessToken: String, refreshToken: String) {
        val session = AuthSession(userId, email, accessToken, refreshToken)
        try {
            encryptedSettings.encodeValue(authSessionSerializer, AUTH_SESSION_KEY, session)
            _authSession.value = session
        } catch (e: Exception) {
            println("SESSION ERROR: Error saving auth session: ${e.message}")
        }
    }

    fun saveUserProfile(state: SetUpUiState) {
        val currentPrefs = getUserPreferences()
        val newPrefs = currentPrefs.copy(setupUiState = state)
        try {
            commonSettings.encodeValue(userPreferencesSerializer, USER_PREFERENCES_KEY, newPrefs)
            _userData.value = state
        } catch (e: Exception) {
            println("SESSION ERROR: Error saving user profile: ${e.message}")
        }
    }

    fun isLoggedIn(): Boolean = _authSession.value != null
    fun getUserId(): String? = _authSession.value?.userId
    fun getAccessToken(): String? = _authSession.value?.accessToken
    fun getRefreshToken(): String? = _authSession.value?.refreshToken

    fun getCurrentUserData(): SetUpUiState = _userData.value

    fun setLanguage(lang: String) {
        val currentPrefs = getUserPreferences()
        val newPrefs = currentPrefs.copy(language = lang)
        commonSettings.encodeValue(userPreferencesSerializer, USER_PREFERENCES_KEY, newPrefs)
        _language.value = lang
        println("LANG: New language: ${language.value}")
    }

    fun setDarkTheme(enabled: Boolean) {
        val currentPrefs = getUserPreferences()
        val newPrefs = currentPrefs.copy(isDarkTheme = enabled)
        commonSettings.encodeValue(userPreferencesSerializer, USER_PREFERENCES_KEY, newPrefs)
        _isDarkTheme.value = enabled
    }
    /**
     * Añade una nueva comida a la plantilla global
     */
    fun addMealToTemplate(meal: Meal) {
        val currentList = _mealTemplate.value.toMutableList()
        // Evitamos duplicados por nombre si es necesario
        if (currentList.none { it.name == meal.name }) {
            currentList.add(meal)
            updateMealTemplate(currentList)
        }
    }

    /**
     * Elimina una comida de la plantilla por su nombre
     */
    fun removeMealFromTemplate(mealName: String) {
        val newList = _mealTemplate.value.filter { it.name != mealName }
        updateMealTemplate(newList)
    }
    fun updateMealTemplate(template: List<Meal>) {
        val currentPrefs = getUserPreferences()
        val newPrefs = currentPrefs.copy(mealTemplate = template)
        commonSettings.encodeValue(userPreferencesSerializer, USER_PREFERENCES_KEY, newPrefs)
        _mealTemplate.value = template
    }
    fun logout() {
        encryptedSettings.remove(AUTH_SESSION_KEY)
        commonSettings.remove(USER_PREFERENCES_KEY)
        clearAll()
    }

    fun clearAll() {
        encryptedSettings.clear()
        commonSettings.clear()
        _authSession.value = null
        _userData.value = SetUpUiState()
        _language.value = UserPreferences().language
        _isDarkTheme.value = UserPreferences().isDarkTheme
        _mealTemplate.value = UserPreferences().mealTemplate
    }

    fun updateTokens(accessToken: String, refreshToken: String) {
        _authSession.value?.let { currentSession ->
            val updatedSession = currentSession.copy(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
            encryptedSettings.encodeValue(AuthSession.serializer(), AUTH_SESSION_KEY, updatedSession)
            _authSession.value = updatedSession
        }
    }
}

@Serializable
data class AuthSession(
    val userId: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String
)

@Serializable
data class UserPreferences(
    val setupUiState: SetUpUiState = SetUpUiState(),
    val language: String = "es",
    val isDarkTheme: Boolean = false,
    val mealTemplate: List<Meal> = listOf(Meal(name = "Meal 1"), Meal(name = "Meal 2"),Meal(name="Meal 3"))
)
