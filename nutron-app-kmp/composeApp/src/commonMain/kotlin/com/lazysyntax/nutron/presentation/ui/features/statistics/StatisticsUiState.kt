package com.lazysyntax.nutron.presentation.ui.features.statistics

import com.himanshoe.charty.line.data.LineData

data class StatisticsUiState(
    val selectedRange: TimeRange = TimeRange.WEEK,
    val caloriesData: List<LineData> = emptyList(),
    val proteinsData: List<LineData> = emptyList(),
    val fatsData: List<LineData> = emptyList(),
    val sugarData: List<LineData> = emptyList(),
    val carbsData: List<LineData> = emptyList(),
    val averageCalories: Double = 0.0,
    val totalCalories: Double = 0.0,
    val averageProteins: Double = 0.0,
    val totalProteins: Double = 0.0,
    val averageFats: Double = 0.0,
    val totalFats: Double = 0.0,
    val averageSugar: Double = 0.0,
    val totalSugar: Double = 0.0,
    val averageCarbs: Double = 0.0,
    val totalCarbs: Double = 0.0,
    val isLoading: Boolean = false,
    val selectedNutrients: Set<NutrientType> = setOf(NutrientType.PROTEINS)
)

enum class NutrientType {
    CALORIES, PROTEINS, FATS, CARBS
}

enum class TimeRange {
    WEEK, MONTH, THREE_MONTHS
}
