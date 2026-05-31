package com.lazysyntax.nutron.data.remote.authentication

import io.ktor.util.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object JwtDecoder {
    private val json = Json { ignoreUnknownKeys = true }

    fun getUserIdFromToken(rawToken: String): String? {
        return try {

            // 1.Formateo de string inicial: Quitar "Bearer " si existe y espacios en blanco
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

            payload = payload.replace('-', '+').replace('_', '/')


            //Corregir el Padding (=)

            val missingPadding = payload.length % 4
            if (missingPadding > 0) {
                payload += "=".repeat(4 - missingPadding)
            }

            val decodedPayload = payload.decodeBase64String()
            val jsonObject = json.parseToJsonElement(decodedPayload).jsonObject


            // El backend construye el Token mediante .subjetct(...) por lo que podemos definir
            // concretamente el campo "sub" como el ID del usuario
            return jsonObject["sub"]?.jsonPrimitive?.content

        } catch (e: Exception) {
             println("Error decodificando JWT: ${e.message}")
            null
        }
    }

}