package com.lazysyntax.nutron.data.services.authentication

import io.ktor.util.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object JwtDecoder {
    private val json = Json { ignoreUnknownKeys = true }

    fun getUserIdFromToken(rawToken: String): String? {
        return try {

            // 1. Limpieza inicial: Quitar "Bearer " si existe y espacios en blanco
            val token = if (rawToken.startsWith("Bearer ", ignoreCase = true)) {
                rawToken.substring(7).trim()
            } else {
                rawToken.trim()
            }


            val parts = token.split(".")
            if (parts.size != 3) {
                println("Error: Formato de token invalido, se esperaban 3 partes " +
                        "(Header.Payload.Signature), pero se recibieron ${parts.size}")
                return null
            }

            var payload = parts[1]

            // 1. Corregir caracteres de Base64URL a Base64 estándar
            payload = payload.replace('-', '+').replace('_', '/')


            // 5. Corregir el Padding (=)
            // Base64 requiere que la longitud sea múltiplo de 4
            val missingPadding = payload.length % 4
            if (missingPadding > 0) {
                payload += "=".repeat(4 - missingPadding)
            }

            val decodedPayload = payload.decodeBase64String()
            val jsonObject = json.parseToJsonElement(decodedPayload).jsonObject

            // MongoDB / JWT estándar
            // El backend construye el Token mediante .subjetct(...) por lo que podemos definir
            // concretamente el campo "sub" como el ID del usuario
            return jsonObject["sub"]?.jsonPrimitive?.content
                /*jsonObject["id"]?.jsonPrimitive?.content
                    ?: jsonObject["sub"]?.jsonPrimitive?.content
                    ?: jsonObject["userId"]?.jsonPrimitive?.content
                    ?: jsonObject["user_id"]?.jsonPrimitive?.content*/

        } catch (e: Exception) {
             println("Error decodificando JWT: ${e.message}")
            null
        }
    }

}