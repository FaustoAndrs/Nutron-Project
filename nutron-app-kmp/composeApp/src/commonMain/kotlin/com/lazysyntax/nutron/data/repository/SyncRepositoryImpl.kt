package com.lazysyntax.nutron.data.repository

import com.lazysyntax.nutron.data.remote.NetworkConstants.AUTH_BASE_URL
import com.lazysyntax.nutron.data.remote.authentication.SessionManager
import com.lazysyntax.nutron.data.remote.synchronization.SyncResult
import com.lazysyntax.nutron.data.remote.synchronization.UserSetupResponse
import com.lazysyntax.nutron.data.remote.synchronization.toSetupUiState
import com.lazysyntax.nutron.domain.repository.SyncRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class SyncRepositoryImpl(
    private val client: HttpClient,
    private val sessionManager: SessionManager
) : SyncRepository {
    private val BASE_URL = "${AUTH_BASE_URL}/user" // para el emulador de Android

    // Agrega esta función para obtener el perfil
    override suspend fun syncUserSetUp(): SyncResult {
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