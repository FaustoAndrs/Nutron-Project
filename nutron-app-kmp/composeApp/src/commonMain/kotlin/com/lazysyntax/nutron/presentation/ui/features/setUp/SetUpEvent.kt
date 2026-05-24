package com.lazysyntax.nutron.presentation.ui.features.setUp

sealed interface SetUpEvent {
    data class WeightChanged(val weight: String) : SetUpEvent
    data class HeightChanged(val height: String) : SetUpEvent
    data class GenderChanged(val gender: Gender) : SetUpEvent
    data class AgeChanged(val age: String) : SetUpEvent
    data class ActivityChanged(val activity: Activity) : SetUpEvent
    data class GoalChanged(val goal: Goal) : SetUpEvent
    data class FormulaChanged(val formula: String) : SetUpEvent
    object OnClickSave : SetUpEvent
    data class OnClickBack(val fromSignUp: Boolean) : SetUpEvent
}
