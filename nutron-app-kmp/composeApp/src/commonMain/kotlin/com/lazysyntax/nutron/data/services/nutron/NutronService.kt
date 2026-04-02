package com.lazysyntax.nutron.data.services.nutron

interface NutronService {
    suspend fun fetchProductMacrosBarcode(barcode: String): Product?
    suspend fun searchProductsByName(name: String): List<Product>
}
