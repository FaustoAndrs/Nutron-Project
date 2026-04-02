package com.lazysyntax.nutron.main.ui.features.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysyntax.nutron.data.services.nutron.NutronService
import com.lazysyntax.nutron.data.services.nutron.Product
import io.ktor.client.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiaryViewModel(
    private val openFoodFactsService: NutronService
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiaryUiState())
    val uiState: StateFlow<DiaryUiState> = _uiState.asStateFlow()

    fun onProductNameFieldChange(productName: String) {
        _uiState.update { it.copy(productName = productName) }
    }

    fun onBarcodeFieldChange(barcode: String) {
        _uiState.update { it.copy(barcode = barcode) }
    }

    fun onSearchBarcode() {
        viewModelScope.launch {
            val product = openFoodFactsService.fetchProductMacrosBarcode(_uiState.value.barcode)
            _uiState.update { it.copy(product = product) }
        }
    }

    fun onSearchProduct() {
        viewModelScope.launch {
            val products: List<Product> = openFoodFactsService.searchProductsByName(_uiState.value.productName)
            _uiState.update { it.copy(productList = products) }
        }
    }
    fun onAddProduct() {
        viewModelScope.launch {
            val products: List<Product> = openFoodFactsService.searchProductsByName(_uiState.value.productName)
            _uiState.update { it.copy(productList = products) }
        }
    }
}
