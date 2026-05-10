package com.lazysyntax.nutron.main.ui.features.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysyntax.nutron.data.services.openFoodFactsApi.OpenFoodFactService
import com.lazysyntax.nutron.main.ui.navigation.Navigator
import com.lazysyntax.nutron.main.ui.navigation.Route
import com.lazysyntax.nutron.models.Food
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant


class DiaryViewModel(
    private val openFoodFactsService: OpenFoodFactService,
    private val navigator: Navigator
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiaryUiState())
    val uiState: StateFlow<DiaryUiState> = _uiState.asStateFlow()


    fun onDiaryEvent(diaryEvent: DiaryEvent) {
        when (diaryEvent) {
            DiaryEvent.OnClickAddProduct -> onAddProduct()
            DiaryEvent.OnClickChangeDate -> onClicChangeDate()
            DiaryEvent.OnDismissDatePicker -> onDismissDatePicker()
            is DiaryEvent.OnDateSelected -> onDateSelected(diaryEvent.dateMillis)
            DiaryEvent.OnClickPreviousDay -> onMoveDay(-1)
            DiaryEvent.OnClickNextDay -> onMoveDay(1)
        }
    }

    private fun onClicChangeDate() {

        _uiState.update { it.copy(isDatePickerVisible = true) }
    }

    private fun onDismissDatePicker() {
        _uiState.update { it.copy(isDatePickerVisible = false) }
    }

    private fun onDateSelected(dateMillis: Long?) {
        dateMillis?.let { millis ->
            val date = Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.UTC).date
            _uiState.update { it.copy(date = date, isDatePickerVisible = false) }
        } ?: _uiState.update { it.copy(isDatePickerVisible = false) }
    }

    private fun onMoveDay(days: Int) {
        _uiState.update {
            val newDate = if (days >= 0) {
                it.date.plus(days, DateTimeUnit.DAY)
            } else {
                it.date.minus(-days, DateTimeUnit.DAY)
            }
            it.copy(date = newDate)
        }
    }

    fun onProductNameFieldChange(productName: String) {
        _uiState.update { it.copy(productName = productName) }
    }

    fun onBarcodeFieldChange(barcode: String) {
        _uiState.update { it.copy(barcode = barcode) }
    }

    fun onSearchBarcode() {
        viewModelScope.launch {
            val product = openFoodFactsService.fetchFoodByBarcode(_uiState.value.barcode)
            _uiState.update { it.copy(food = product) }
        }
    }

    fun onSearchProduct() {
        viewModelScope.launch {
            val foods: List<Food> =
                openFoodFactsService.searchFoodByName(_uiState.value.productName)
            _uiState.update { it.copy(foodList = foods) }
        }
    }

    fun onProductSelected(product: Food) {
        _uiState.update { it.copy(food = product) }
    }


    fun onAddProduct() {
        navigator.navigateTo(Route.Library)
    }
}
