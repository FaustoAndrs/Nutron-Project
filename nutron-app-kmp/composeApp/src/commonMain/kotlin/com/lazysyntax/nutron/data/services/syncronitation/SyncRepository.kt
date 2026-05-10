package com.lazysyntax.nutron.data.services.syncronitation

import com.lazysyntax.nutron.data.services.authentication.SessionManager
import com.lazysyntax.nutron.main.ui.features.setUp.SetUpUiState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class SyncRepository(
    private val client: HttpClient,
    private val sessionManager: SessionManager
) {
    private val BASE_URL = "http://10.0.2.2:8081/api/v1/user" // para el emulador de Android

    // Agrega esta función para obtener el perfil
    suspend fun syncUserSetUp(): SyncResult {
        return try {

            val response = client.get("$BASE_URL/setup"){
                contentType(ContentType.Application.Json)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val settings = response.body<UserSetupResponse>()
                    // Guardamos en local usando el converter que ya tienes
                    sessionManager.saveUserProfile(settings.toSetupUiState())
                    SyncResult.Success
                }
                HttpStatusCode.NotFound -> {
                    // El servidor responde 404 porque el usuario no ha creado su perfil
                    SyncResult.NotFound
                }
                HttpStatusCode.Unauthorized -> {
                    // Aunque el plugin Auth maneja el refresh, si el refresh también falla,
                    // recibiremos un 401 aquí.
                    SyncResult.Error
                }
                else -> {
                    // Cualquier otro error (401, 500, etc.)
                    SyncResult.Error
                }
            }
        } catch (e: Exception) {
            println("Error al sincronizar: ${e.message}")
            SyncResult.Error
        }
    }

}

