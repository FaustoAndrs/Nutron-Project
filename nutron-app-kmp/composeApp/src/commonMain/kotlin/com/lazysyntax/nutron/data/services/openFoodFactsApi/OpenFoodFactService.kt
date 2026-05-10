package com.lazysyntax.nutron.data.services.openFoodFactsApi

import com.lazysyntax.nutron.models.Food

interface OpenFoodFactService {
    suspend fun fetchFoodByBarcode(barcode: String): Food?
    suspend fun searchFoodByName(name: String): List<Food>
}
