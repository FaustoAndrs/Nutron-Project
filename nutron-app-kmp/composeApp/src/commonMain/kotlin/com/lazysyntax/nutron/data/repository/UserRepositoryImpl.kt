package com.lazysyntax.nutron.data.repository

import com.lazysyntax.nutron.data.remote.NetworkConstants
import com.lazysyntax.nutron.data.remote.authentication.SessionManager
import com.lazysyntax.nutron.data.remote.synchronization.UserSetupResponse
import com.lazysyntax.nutron.data.remote.synchronization.toSetupUiState
import com.lazysyntax.nutron.data.remote.synchronization.toTargetDto
import com.lazysyntax.nutron.data.remote.synchronization.toUserSetupEntity
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

class UserRepositoryImpl(
    private val client: HttpClient,
    private val sessionManager: SessionManager,
) : UserRepository {


    //private val BASE_URL = "http://localhost:8081/api/v1" // para iOS o Desktop
    private val BASE_URL = NetworkConstants.AUTH_BASE_URL // para el emulador de Android

    override suspend fun getUserSetups(id: String): Boolean {

        // 1. Obtener el token guardado en la sesión
//        val token = sessionManager.getAccessToken()

        return try {
            //println("Intentando obtener los settings del usuario: $token en  $BASE_URL/user/settings")

            val response = client.get("$BASE_URL/user/setup") {
                contentType(ContentType.Application.Json)
            }
            if (response.status == HttpStatusCode.OK) {
                val settings = response.body<UserSetupResponse>()

                // 4. Persistir localmente si es necesario
                val now = Clock.System.now().toEpochMilliseconds()
                sessionManager.saveUserProfile(settings.toSetupUiState(), lastSyncTime = now)
                true
            } else {
                val errorBody =
                    response.body<String>() // Intentar leer el mensaje de error del backend
                println("Error al obtener settings. Código: ${response.status}, Error: $errorBody")
                false
            }

        } catch (e: Exception) {
            println("Fallo de red: ${e.message}")
            false
        }
    }



    override suspend fun updateUserSetup(setUpUiState: SetUpUiState): Boolean {
        println(
            "REPO DEBUG: Actualizando datos." +
                    " Usuario en sesión: ${sessionManager.getUserId()} - " +
                    "Email: ${sessionManager.getAuthSession()?.email}"
        )
        return try {
            val response = client.put("$BASE_URL/user/setup") {
                contentType(ContentType.Application.Json)
                setBody(setUpUiState.toUserSetupEntity())
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
