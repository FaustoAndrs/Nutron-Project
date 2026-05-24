package com.lazysyntax.nutron.presentation.ui.features.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.himanshoe.charty.line.data.LineData
import com.lazysyntax.nutron.domain.models.Food
import com.lazysyntax.nutron.domain.models.Meal
import com.lazysyntax.nutron.domain.repository.MealRepository
import com.lazysyntax.nutron.presentation.ui.navigation.Navigator
import kotlinx.coroutines.Job
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

    private var loadDataJob: Job? = null

    init {
        loadData(TimeRange.WEEK)
    }

    fun onEvent(event: StatisticsEvent) {
        when (event) {
            is StatisticsEvent.OnRangeSelected -> {
                if (_uiState.value.selectedRange != event.range) {
                    _uiState.update { it.copy(selectedRange = event.range) }
                    loadData(event.range)
                }
            }

            is StatisticsEvent.OnNutrientToggled -> {
                _uiState.update { state ->
                    val current = state.selectedNutrients
                    val newSelection = if (current.contains(event.nutrient)) {
                        if (current.size > 1) current - event.nutrient else current
                    } else {
                        current + event.nutrient
                    }
                    state.copy(selectedNutrients = newSelection)
                }
            }

            StatisticsEvent.OnClickBack -> navigator.goBack()
        }
    }

    private fun loadData(range: TimeRange) {
        loadDataJob?.cancel()
        val today = System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val startDate = calculateStartDate(today, range)

        loadDataJob = viewModelScope.launch {
            mealRepository.getMealsByDateRange(startDate, today).collectLatest { meals ->
                val groupedByDate = meals.groupBy { it.date }
                val dateList = generateDateList(startDate, today)

                val caloriesChart = processNutrientData(dateList, groupedByDate) { it.nutriments?.calories ?: 0.0 }
                val proteinsChart = processNutrientData(dateList, groupedByDate) { it.nutriments?.proteins ?: 0.0 }
                val carbsChart = processNutrientData(dateList, groupedByDate) { it.nutriments?.carbs ?: 0.0 }
                val fatsChart = processNutrientData(dateList, groupedByDate) { it.nutriments?.fat ?: 0.0 }
                val sugarChart = processNutrientData(dateList, groupedByDate) { it.nutriments?.sugars ?: 0.0 }

                _uiState.update { state ->
                    state.copy(
                        caloriesData = caloriesChart.data,
                        totalCalories = caloriesChart.total,
                        averageCalories = caloriesChart.average,
                        proteinsData = proteinsChart.data,
                        totalProteins = proteinsChart.total,
                        averageProteins = proteinsChart.average,
                        fatsData = fatsChart.data,
                        totalFats = fatsChart.total,
                        averageFats = fatsChart.average,
                        carbsData = carbsChart.data,
                        totalCarbs = carbsChart.total,
                        averageCarbs = carbsChart.average,
                        sugarData = sugarChart.data,
                        totalSugar = sugarChart.total,
                        averageSugar = sugarChart.average
                    )
                }
            }
        }
    }

    private fun calculateStartDate(today: LocalDate, range: TimeRange): LocalDate {
        return when (range) {
            TimeRange.WEEK -> today.minus(7, DateTimeUnit.DAY)
            TimeRange.MONTH -> today.minus(1, DateTimeUnit.MONTH)
            TimeRange.THREE_MONTHS -> today.minus(3, DateTimeUnit.MONTH)
        }
    }

    private fun generateDateList(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
        val dateList = mutableListOf<LocalDate>()
        var current = startDate
        while (current <= endDate) {
            dateList.add(current)
            current = current.plus(1, DateTimeUnit.DAY)
        }
        return dateList
    }

    private fun processNutrientData(
        dateList: List<LocalDate>,
        groupedMeals: Map<LocalDate?, List<Meal>>,
        nutrientSelector: (Food) -> Double
    ): NutrientResult {
        val chartData = dateList.map { date ->
            val totalValue = groupedMeals[date]?.sumOf { meal ->
                meal.foods?.sumOf { nutrientSelector(it) } ?: 0.0
            } ?: 0.0
            LineData(
                label = "${date.dayOfMonth}/${date.month.number}",
                value = totalValue.toFloat()
            )
        }

        val total = chartData.sumOf { it.value.toDouble() }
        val average = if (chartData.isNotEmpty()) total / chartData.size else 0.0

        return NutrientResult(chartData, total, average)
    }

    private data class NutrientResult(
        val data: List<LineData>,
        val total: Double,
        val average: Double
    )
}
