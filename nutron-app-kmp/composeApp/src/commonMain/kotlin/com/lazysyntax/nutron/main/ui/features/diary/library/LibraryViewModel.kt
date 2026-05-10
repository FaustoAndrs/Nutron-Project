package com.lazysyntax.nutron.main.ui.features.diary.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysyntax.nutron.data.repository.FoodRepository
import com.lazysyntax.nutron.data.repository.MealRepository
import com.lazysyntax.nutron.main.ui.navigation.Navigator
import com.lazysyntax.nutron.models.Food
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val foodRepository: FoodRepository,
    private val mealRepository: MealRepository,
    private val navigator: Navigator
) : ViewModel() {
    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    fun onLibraryEvent(libraryEvent: LibraryEvent){
        when(libraryEvent){
            is LibraryEvent.BarcodeChanged -> onBarcodeFieldChange(libraryEvent.barcode)
            LibraryEvent.OnClickSearchBarcode -> onSearchBarcode()
            LibraryEvent.OnClickSearchProduct -> onSearchProduct()
            is LibraryEvent.ProductNameChanged -> onProductNameFieldChange(libraryEvent.productName)
            is LibraryEvent.ProductSelected -> onFoodSelected(libraryEvent.product)
            LibraryEvent.OnClickBack -> onBack()
        }
    }

    private fun onBack() {
        navigator.goBack()
    }



    // Alimentos guardados localmente expuestos como StateFlow
    val savedFoods: StateFlow<List<Food>> = foodRepository.getSavedFoods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onProductNameFieldChange(productName: String) {
        _uiState.update { it.copy(productName = productName) }
    }

    fun onBarcodeFieldChange(barcode: String) {
        _uiState.update { it.copy(barcode = barcode) }
    }

    fun onSearchBarcode() {
        viewModelScope.launch {
            val product = foodRepository.fetchFoodByBarcode(_uiState.value.barcode)
            _uiState.update { it.copy(food = product) }
        }
    }

    fun onSearchProduct() {
        viewModelScope.launch {
            val foods = foodRepository.searchFoodByName(_uiState.value.productName)
            _uiState.update { it.copy(foodList = foods) }
        }
    }

    // Al seleccionar un alimento de la búsqueda para guardarlo localmente
    fun onFoodSelected(food: Food) {
        viewModelScope.launch {
            foodRepository.saveFood(food)
        }
    }

    fun onDeleteFood(code: String) {
        viewModelScope.launch {
            foodRepository.deleteFood(code)
        }
    }

    fun onCreateMeal(name: String, foods: List<Food>) {
        viewModelScope.launch {
            mealRepository.createMeal(name, foods)
        }
    }
}
