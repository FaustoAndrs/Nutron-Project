package com.lazysyntax.nutron.presentation.ui.features.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshoe.charty.line.data.LineData
import com.lazysyntax.nutron.domain.repository.MealRepository
import com.lazysyntax.nutron.presentation.ui.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock.System

class StatisticsViewModel(
    private val mealRepository: MealRepository,
    private val navigator: Navigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        loadData(TimeRange.WEEK)
    }

    fun onEvent(event: StatisticsEvent) {
        when (event) {
            is StatisticsEvent.OnRangeSelected -> {
                _uiState.update { it.copy(selectedRange = event.range) }
                loadData(event.range)
            }
            StatisticsEvent.OnClickBack -> navigator.goBack()
        }
    }

    private fun loadData(range: TimeRange) {
        val today = System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val startDate = when (range) {
            TimeRange.WEEK -> today.minus(7, DateTimeUnit.DAY)
            TimeRange.MONTH -> today.minus(1, DateTimeUnit.MONTH)
            TimeRange.THREE_MONTHS -> today.minus(3, DateTimeUnit.MONTH)
        }

        viewModelScope.launch {
            mealRepository.getMealsByDateRange(startDate, today).collectLatest { meals ->
                val groupedByDate = meals.groupBy { it.date }
                
                val dateList = mutableListOf<LocalDate>()
                var current = startDate
                while (current <= today) {
                    dateList.add(current)
                    current = current.plus(1, DateTimeUnit.DAY)
                }

                val chartData = dateList.map { date ->
                    val totalCals = groupedByDate[date]?.sumOf { meal ->
                        meal.foods?.sumOf { it.nutriments?.calories ?: 0.0 } ?: 0.0
                    } ?: 0.0
                    LineData(
                        label = "${date.day}/${date.month.number}",
                        value = totalCals.toFloat()
                    )
                }

                val total = chartData.sumOf { it.value.toDouble() }
                val average = if (chartData.isNotEmpty()) total / chartData.size else 0.0

                _uiState.update { 
                    it.copy(
                        caloriesData = chartData,
                        totalCalories = total,
                        averageCalories = average
                    )
                }
            }
        }
    }
}
