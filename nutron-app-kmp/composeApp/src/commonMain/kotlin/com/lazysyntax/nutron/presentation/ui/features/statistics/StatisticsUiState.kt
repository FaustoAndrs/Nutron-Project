package com.lazysyntax.nutron.presentation.ui.features.statistics

import com.himanshoe.charty.line.data.LineData

data class StatisticsUiState(
    val selectedRange: TimeRange = TimeRange.WEEK,
    val caloriesData: List<LineData> = emptyList(),
    val isLoading: Boolean = false,
    val averageCalories: Double = 0.0,
    val totalCalories: Double = 0.0
)

enum class TimeRange {
    WEEK, MONTH, THREE_MONTHS
}
