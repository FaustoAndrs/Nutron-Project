package com.lazysyntax.nutron.data.remote.openFoodFactsApi

import com.lazysyntax.nutron.domain.models.Food

interface OpenFoodFactService {
    suspend fun fetchFoodByBarcode(barcode: String): Food?
    suspend fun searchFoodByName(name: String): List<Food>
}
