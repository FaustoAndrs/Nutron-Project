package com.lazysyntax.nutron.data.services.nutron

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class OpenFoodFactsServiceImpl(private val client: HttpClient) : NutronService {
    private val userAgent = "NutronApp - Android - Version 1.0 - Contact: fausto1884@gmail.com"
    //3017624010701
    override suspend fun fetchProductMacrosBarcode(barcode: String): Product? {
        val fields = "product_name,nutriments"
        val url = "https://world.openfoodfacts.net/api/v2/product/$barcode?fields=$fields"

        return try {
            val response = client.get(url) {
                header(HttpHeaders.UserAgent, userAgent)
            }

            if (response.status.isSuccess()) {
                val productResponse: ProductResponse = response.body()
                if (productResponse.status == 1) productResponse.product else null
            } else {
                println("Error OFF Barcode: ${response.status.value}")
                null
            }
        } catch (e: Exception) {
            println("Error en la petición por código de barras: ${e.message}")
            null
        }
    }

    override suspend fun searchProductsByName(name: String): List<Product> {
        val fields = "product_name,nutriments"
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
                searchResponse.products ?: emptyList()
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
