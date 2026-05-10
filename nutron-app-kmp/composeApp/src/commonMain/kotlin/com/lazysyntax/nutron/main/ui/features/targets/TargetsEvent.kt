package com.lazysyntax.nutron.main.ui.features.targets

import com.lazysyntax.nutron.main.ui.features.targets.composables.Diet

sealed interface TargetsEvent {
    data class OnSelectDietType(val diet: Diet) : TargetsEvent
    object OnEditMealsDistribution : TargetsEvent
    data class OnClickBack(val fromSignUp: Boolean) : TargetsEvent
}
