package com.lazysyntax.nutron.data.repository

import com.lazysyntax.nutron.data.remote.NetworkConstants
import com.lazysyntax.nutron.data.remote.authentication.SessionManager
import com.lazysyntax.nutron.data.remote.synchronization.UserSetupResponse
import com.lazysyntax.nutron.data.remote.synchronization.toSetupUiState
import com.lazysyntax.nutron.data.remote.synchronization.toTargetDto
import com.lazysyntax.nutron.data.remote.synchronization.toUserSetupDto
import com.lazysyntax.nutron.domain.repository.UserRepository
import com.lazysyntax.nutron.presentation.ui.features.setUp.SetUpUiState
import com.lazysyntax.nutron.presentation.ui.features.targets.TargetsUiState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.time.Clock

class UserSetupRepositoryImpl(
    private val client: HttpClient,
    private val sessionManager: SessionManager,
) : UserRepository {


    //private val BASE_URL = "http://localhost:8081/api/v1" // para iOS o Desktop
    private val BASE_URL = NetworkConstants.AUTH_BASE_URL // para el emulador de Android


    override suspend fun updateUserSetup(setUpUiState: SetUpUiState): Boolean {
        println(
            "REPO DEBUG: Actualizando datos." +
                    " Usuario en sesión: ${sessionManager.getUserId()} - " +
                    "Email: ${sessionManager.getAuthSession()?.email}"
        )
        return try {
            val response = client.put("$BASE_URL/user/setup") {
                contentType(ContentType.Application.Json)
                setBody(setUpUiState.toUserSetupDto())
            }

            if (response.status == HttpStatusCode.OK) {
                val now = Clock.System.now().toEpochMilliseconds()
                sessionManager.saveUserProfile(setUpUiState, lastSyncTime = now)
                true
            } else {
                val errorBody = response.body<String>()
                println("Error al actualizar settings: ${response.status}, $errorBody")

                false
            }
        } catch (e: Exception) {
            println("Error de red al actualizar: ${e.message}")
            false
        }

    }

    override suspend fun updateUserDiet(targetsUiState: TargetsUiState): Boolean {
        println(
            "REPO DEBUG: Actualizando datos." +
                    " Usuario en sesión: ${sessionManager.getUserId()} - " +
                    "Email: ${sessionManager.getAuthSession()?.email}"
        )
        return try {
            val response = client.patch("$BASE_URL/user/setup/diet") {
                contentType(ContentType.Application.Json)
                setBody(targetsUiState.toTargetDto())
            }

            if (response.status == HttpStatusCode.OK) {
                val currentSetup = sessionManager.getCurrentUserData()
                val updatedSetup = currentSetup.copy(diet = targetsUiState.diet.name)
                val now = Clock.System.now().toEpochMilliseconds()
                sessionManager.saveUserProfile(updatedSetup, lastSyncTime = now)
                true
            } else {
                val errorBody = response.body<String>()
                println("Error al actualizar settings: ${response.status}, $errorBody")

                false
            }
        } catch (e: Exception) {
            println("Error de red al actualizar: ${e.message}")
            false
        }

    }
}
