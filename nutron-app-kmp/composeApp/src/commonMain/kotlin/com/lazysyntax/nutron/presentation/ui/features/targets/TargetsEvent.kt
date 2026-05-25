package com.lazysyntax.nutron.presentation.ui.features.targets

import com.lazysyntax.nutron.presentation.ui.features.targets.composables.Diet


sealed interface TargetsEvent {
    data class OnSelectDietType(val diet: Diet) : TargetsEvent
    data class OnAddCustomDiet(val diet: Diet) : TargetsEvent
    object OnEditMealsDistribution : TargetsEvent
    data class OnClickBack(val fromSignUp: Boolean) : TargetsEvent
    object OnNavigateToStatistics : TargetsEvent
    object OnNavigateToProfile : TargetsEvent
}
