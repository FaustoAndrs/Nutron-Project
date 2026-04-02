package com.lazysyntax.nutron.data.services.authentication

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthRepository(private val client: HttpClient) {

    /*
    * Recordatorio crítico de Red: > * Si usas el Emulador de Android, la URL será 10.0.2.2:8081.
    * Si usas iOS o Desktop, será localhost:8081.
    * Cambia "localhost" por "10.0.2.2" si pruebas en Android Emulator
     * */
    private val BASE_URL = "http://localhost:8081/api/auth"

    suspend fun login(username: String, password: String): Boolean {
        return try {
            val response = client.post("$BASE_URL/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(username, password))
            }
            response.status == HttpStatusCode.OK
        } catch (e: Exception) {
            false
        }
    }

    suspend fun register(username: String,fullname: String, email: String, password: String): Boolean {
        return try {
            val response = client.post("$BASE_URL/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(username, fullname, email, password))
            }
            response.status == HttpStatusCode.OK
        } catch (e: Exception) {
            false
        }
    }
}
