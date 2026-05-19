package com.lazysyntax.nutron.data.remote.food

import com.lazysyntax.nutron.data.remote.NetworkConstants.DATA_BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class FoodRemoteDataSource(
    private val client: HttpClient
) {
    private val BASE_URL = "${DATA_BASE_URL}/foods"

    suspend fun saveFood(foodDto: FoodDto): Boolean {
        return try {
            val response = client.post(BASE_URL) {
                contentType(ContentType.Application.Json)
                println("ENVIADO A DB-NUTRITION: $foodDto")
                setBody(foodDto)
            }
            response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getFoodsByUser(userId: String): List<FoodDto> {
        return try {
            val response = client.get("$BASE_URL/user/$userId") {
                contentType(ContentType.Application.Json)
            }
            if (response.status == HttpStatusCode.OK) {
                response.body<List<FoodDto>>()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
