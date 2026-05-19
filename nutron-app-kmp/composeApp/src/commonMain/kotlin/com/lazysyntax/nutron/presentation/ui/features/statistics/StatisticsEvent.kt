package com.lazysyntax.nutron.presentation.ui.features.statistics

sealed interface StatisticsEvent {
    data class OnRangeSelected(val range: TimeRange) : StatisticsEvent
    data object OnClickBack : StatisticsEvent
}
