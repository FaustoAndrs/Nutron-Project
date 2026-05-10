package com.lazysyntax.nutron.data.services.authentication

import com.lazysyntax.nutron.main.ui.features.setUp.SetUpUiState
import com.lazysyntax.nutron.main.ui.features.targets.TargetsUiState
import com.lazysyntax.nutron.models.TargetEntity
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

    private val _authSession = MutableStateFlow(getAuthSession())
    val authSession: StateFlow<AuthSession?> = _authSession.asStateFlow()

    private val _userData = MutableStateFlow(getUserPreferences().setupUiState)
    val userData: StateFlow<SetUpUiState> = _userData.asStateFlow()

    private val _language = MutableStateFlow(getUserPreferences().language)
    val language: StateFlow<String> = _language.asStateFlow()

    fun getAuthSession(): AuthSession? = encryptedSettings.decodeValue(
        authSessionSerializer,
        AUTH_SESSION_KEY,
        null
    )

    fun getUserPreferences(): UserPreferences = commonSettings.decodeValue(
        userPreferencesSerializer,
        USER_PREFERENCES_KEY,
        UserPreferences()
    )

    fun saveSession(userId: String, email: String, accessToken: String, refreshToken: String) {
        val session = AuthSession(userId, email, accessToken, refreshToken)
        encryptedSettings.encodeValue(authSessionSerializer, AUTH_SESSION_KEY, session)
        _authSession.value = session
    }

    fun saveUserProfile(state: SetUpUiState) {
        val currentPrefs = getUserPreferences()
        val newPrefs = currentPrefs.copy(setupUiState = state)
        commonSettings.encodeValue(userPreferencesSerializer, USER_PREFERENCES_KEY, newPrefs)
        _userData.value = state
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
    val theme: String = "system"
)
