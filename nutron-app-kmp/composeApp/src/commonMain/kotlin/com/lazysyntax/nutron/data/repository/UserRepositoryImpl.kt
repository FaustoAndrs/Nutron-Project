package com.lazysyntax.nutron.data.repository

import com.lazysyntax.nutron.data.services.authentication.SessionManager
import com.lazysyntax.nutron.data.services.syncronitation.UserSetupResponse
import com.lazysyntax.nutron.data.services.syncronitation.toSetupUiState
import com.lazysyntax.nutron.data.services.syncronitation.toTargetEntity
import com.lazysyntax.nutron.data.services.syncronitation.toUserSetupEntity
import com.lazysyntax.nutron.main.ui.features.setUp.SetUpUiState
import com.lazysyntax.nutron.main.ui.features.targets.TargetsUiState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class UserRepositoryImpl(
    private val client: HttpClient,
    private val sessionManager: SessionManager,
) : UserRepository {


    //private val BASE_URL = "http://localhost:8081/api/v1" // para iOS o Desktop
    private val BASE_URL = "http://10.0.2.2:8081/api/v1" // para el emulador de Android

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
                sessionManager.saveUserProfile(settings.toSetupUiState())
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
                sessionManager.saveUserProfile(setUpUiState)
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
                setBody(targetsUiState.toTargetEntity())
            }

            if (response.status == HttpStatusCode.OK) {
                val currentSetup = sessionManager.getCurrentUserData()
                val updatedSetup = currentSetup.copy(diet = targetsUiState.diet.name)
                sessionManager.saveUserProfile(updatedSetup)
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
