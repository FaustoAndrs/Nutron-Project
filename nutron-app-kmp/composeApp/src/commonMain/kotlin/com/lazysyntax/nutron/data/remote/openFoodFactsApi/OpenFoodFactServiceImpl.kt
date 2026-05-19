package com.lazysyntax.nutron.data.remote.openFoodFactsApi

import com.lazysyntax.nutron.domain.models.Food
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class OpenFoodFactsServiceImpl(private val client: HttpClient) : OpenFoodFactService {
    private val userAgent = "NutronApp - Android - Version 1.0 - Contact: fausto1884@gmail.com"
    //4056489044673
    override suspend fun fetchFoodByBarcode(barcode: String): Food? {
        val fields = "code,product_name,product_name_es,product_name_en,nutriments,nutriscore_grade,brands"
        val url = "https://world.openfoodfacts.net/api/v2/product/$barcode?fields=$fields"

        return try {
            val response = client.get(url) {
                header(HttpHeaders.UserAgent, userAgent)
            }

            if (response.status == HttpStatusCode.OK) {
                val productResponse: FetchResponse = response.body()
                if (productResponse.status == 1) {
                    productResponse.food
                }
                else {
                    null
                }

            } else {
                println("Error OFF Barcode: ${response.status.value}")
                null
            }
        } catch (e: Exception) {
            println("Error en la petición por código de barras: ${e.message}")
            null
        }
    }

    override suspend fun searchFoodByName(name: String): List<Food> {
        val fields = "code,product_name,product_name_es,product_name_en,nutriments,nutriscore_grade,brands"
        val url = "https://world.openfoodfacts.net/cgi/search.pl"

        return try {
            val response = client.get(url) {
                parameter("search_terms", name)
                parameter("search_simple", "1")
                parameter("action", "process")
                parameter("json", "1")
                parameter("fields", fields)
                header(HttpHeaders.UserAgent, userAgent)
                header(HttpHeaders.Accept, "application/json")
            }

            if (response.status.isSuccess()) {
                val searchResponse: SearchResponse = response.body()
                searchResponse.foods ?: emptyList()
            } else {
                println("Error OFF Search: Status ${response.status.value} - ${response.status.description}")
                emptyList()
            }
        } catch (e: Exception) {
            println("Error en la búsqueda por nombre: ${e.message}")
            emptyList()
        }
    }
}
