package com.lazysyntax.nutron.data.services.authentication

import com.lazysyntax.nutron.models.NewUser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.clearAuthTokens
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class AuthRepository(
    private val client: HttpClient, private val sessionManager: SessionManager
) {

    private val BASE_URL = "http://10.0.2.2:8081/api/v1/auth" // para el emulador de Android

    suspend fun login(email: String, password: String): Boolean {
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
                true
            } else {
                val errorBody = response.body<String>()
                println("Login fallido. Código: ${response.status}, Error: $errorBody")
                false
            }
        } catch (e: Exception) {
            println("EXCEPCIÓN en login: ${e.message}")
            e.printStackTrace()
            false
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

    suspend fun register(newUser: NewUser): Boolean {
        return try {
            val response = client.post("$BASE_URL/register") {
                contentType(ContentType.Application.Json)
                setBody(newUser)
            }
            println("Respuesta del servidor: ${response.status}")

            if (response.status != HttpStatusCode.OK) {
                val errorBody = response.body<String>()
                println("register fallido. Código: ${response.status}, Error: $errorBody")
            }

            response.status == HttpStatusCode.OK
        } catch (e: Exception) {
            println("EXCEPCIÓN en SignUp: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    fun logout() {
        client.clearAuthTokens()
        sessionManager.logout()
    }
}
