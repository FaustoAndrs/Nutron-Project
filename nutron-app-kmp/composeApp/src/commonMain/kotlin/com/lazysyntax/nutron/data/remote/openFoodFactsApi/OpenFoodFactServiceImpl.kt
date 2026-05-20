package com.lazysyntax.nutron.data.remote.openFoodFactsApi

import com.lazysyntax.nutron.domain.models.Food
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class OpenFoodFactsServiceImpl(private val client: HttpClient) : OpenFoodFactService {
    private val userAgent = "NutronApp - Android - Version 1.0 - Contact: fausto1884@gmail.com"
    private val fields = "code,product_name,product_name_es,product_name_en,nutriments,nutriscore_grade,brands"

    override suspend fun fetchFoodByBarcode(barcode: String): Food? {
        // Usamos el dominio .net para staging/testing
        val url = "https://world.openfoodfacts.net/api/2/product/$barcode.json"

        return try {
            val response = client.get(url) {
                parameter("fields", fields)
                header(HttpHeaders.UserAgent, userAgent)
                header(HttpHeaders.Accept, "application/json")
            }

            if (response.status.isSuccess()) {
                val productResponse: FetchResponse = response.body()
                if (productResponse.status == 1) {
                    productResponse.food
                } else {
                    null
                }
            } else {
                println("Error OFF Barcode (Staging): Status ${response.status.value}")
                null
            }
        } catch (e: Exception) {
            println("Error en la petición por código de barras (Staging): ${e.message}")
            null
        }
    }

    override suspend fun searchFoodByName(name: String): List<Food> {
        // Usamos el dominio .net para staging/testing
        val url = "https://world.openfoodfacts.net/api/v2/search"

        return try {
            val response = client.get(url) {
                parameter("search_terms", name)
                parameter("fields", fields)
                header(HttpHeaders.UserAgent, userAgent)
                header(HttpHeaders.Accept, "application/json")
            }

            if (response.status.isSuccess()) {
                val searchResponse: SearchResponse = response.body()
                searchResponse.foods ?: emptyList()
            } else {
                println("Error OFF Search (Staging): Status ${response.status.value} - ${response.status.description}")
                emptyList()
            }
        } catch (e: Exception) {
            println("Error en la búsqueda por nombre (Staging): ${e.message}")
            emptyList()
        }
    }
}
