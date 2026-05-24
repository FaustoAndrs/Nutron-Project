package com.lazysyntax.nutron.data.remote.meal

import com.lazysyntax.nutron.data.remote.NetworkConstants.DATA_BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class MealRemoteDataSource(
    private val client: HttpClient
) {
    private val BASE_URL = "${DATA_BASE_URL}/meals" // Adaptar según el backend

    suspend fun saveMeal(mealDto: MealDto): Boolean {
        return try {
            val response = client.post(BASE_URL) {
                contentType(ContentType.Application.Json)
                println("SAVING MEAL DTO : $mealDto ")
                setBody(mealDto)
            }
            response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created
        } catch (e: Exception) {
            println("Error saving meal remotely: ${e.message}")
            false
        }
    }

    suspend fun getMealsByUser(): List<MealDto> {
        return try {
            val response = client.get("$BASE_URL/me") {
                contentType(ContentType.Application.Json)
            }
            if (response.status == HttpStatusCode.OK) {
                response.body<List<MealDto>>()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            println("Error fetching meals remotely: ${e.message}")
            emptyList()
        }
    }
}
