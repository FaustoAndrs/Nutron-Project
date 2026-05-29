package com.lazysyntax.nutron.data.remote.authentication

import com.lazysyntax.nutron.data.remote.NetworkConstants
import com.lazysyntax.nutron.domain.models.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.auth.clearAuthTokens
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AuthRepository(
    private val client: HttpClient,
    private val sessionManager: SessionManager
) {
    private val BASE_URL = "${NetworkConstants.AUTH_BASE_URL}/auth"

    suspend fun login(email: String, password: String): AuthResult {
        return try {
            println("Intentando login para: $email en $BASE_URL/login")

            val response = client.post("$BASE_URL/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, password))
            }
            println("Respuesta del servidor: ${response.status}")

            if (response.status == HttpStatusCode.OK) {
                val tokens = response.body<TokenResponse>()
                val userId = JwtDecoder.getUserIdFromToken(tokens.accessToken)

                println("Token decodificado - UserId: $userId")

                // Llamada corregida sin el parámetro isLoggedIn
                sessionManager.saveSession(
                    userId = userId ?: "",
                    email = email,
                    refreshToken = tokens.refreshToken,
                    accessToken = tokens.accessToken
                )
                AuthResult.Success
            } else {
                val errorBody = response.body<String>()
                println("Login fallido. Código: ${response.status}, Error: $errorBody")
                AuthResult.InvalidCredentials
            }
        } catch (e: HttpRequestTimeoutException) {
            println("TIMEOUT: El servidor tardó demasiado en responder.")
            AuthResult.NetworkError
        } catch (e: Exception) {
            println("EXCEPCIÓN en login: ${e.message}")
            e.printStackTrace()
            if (e.isNetworkError()) {
                AuthResult.NetworkError
            } else {
                AuthResult.UnknownError(e.message)
            }
        }
    }

    suspend fun refreshToken(refreshToken: String): TokenResponse? {
        return try {
            val response = client.post("$BASE_URL/refresh") {
                contentType(ContentType.Application.Json)
                setBody(refreshToken)
            }

            if (response.status == HttpStatusCode.OK) {
                response.body<TokenResponse>()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun register(newUser: User): AuthResult {
        return try {
            val response = client.post("$BASE_URL/register") {
                contentType(ContentType.Application.Json)
                setBody(newUser)
            }
            println("Respuesta del servidor: ${response.status}")

            when (response.status) {
                HttpStatusCode.OK,
                HttpStatusCode.Created -> {
                    AuthResult.Success
                }
                HttpStatusCode.Conflict -> {
                    println("register fallido. El usuario ya existe (409).")
                    AuthResult.UserAlreadyExists
                }
                else -> {
                    val errorBody = response.body<String>()
                    println("register fallido. Código: ${response.status}, Error: $errorBody")
                    AuthResult.InvalidCredentials
                }
            }
        } catch (e: HttpRequestTimeoutException) {
            println("TIMEOUT en SignUp: El servidor tardó demasiado.")
            AuthResult.NetworkError
        } catch (e: Exception) {
            println("EXCEPCIÓN en SignUp: ${e.message}")
            e.printStackTrace()
            if (e.isNetworkError()) {
                AuthResult.NetworkError
            } else {
                AuthResult.UnknownError(e.message)
            }
        }
    }
    @OptIn(ExperimentalUuidApi::class)
    fun loginAsGuest(){
        //Se guarda una session solamente con un UUID
        sessionManager.saveSession(
            userId = Uuid.random().toString(),
            email = null,
            accessToken = null,
            refreshToken = null
        )
        //habilitamos el Flag de usuario invitado
        sessionManager.setGuestLogin(true)
    }
    fun logout() {
        client.clearAuthTokens()
        sessionManager.logout()
    }
}

// Función de extensión para detectar errores de red en KMP
private fun Throwable.isNetworkError(): Boolean {
    val name = this::class.simpleName ?: ""
    val message = this.message ?: ""
    return name.contains("ConnectException", ignoreCase = true) ||
            name.contains("SocketException", ignoreCase = true) ||
            name.contains("UnresolvedAddressException", ignoreCase = true) ||
            message.contains("ConnectException", ignoreCase = true) ||
            message.contains("Failed to connect", ignoreCase = true)
}
