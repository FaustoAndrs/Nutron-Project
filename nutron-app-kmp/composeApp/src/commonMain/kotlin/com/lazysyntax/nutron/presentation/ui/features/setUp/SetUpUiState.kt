package com.lazysyntax.nutron.presentation.ui.features.setUp


import kotlinx.serialization.Serializable
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.gain_weight_objective
import nutron.composeapp.generated.resources.gain_weight_slowly_objective
import nutron.composeapp.generated.resources.high_activity_level
import nutron.composeapp.generated.resources.lose_weight_objective
import nutron.composeapp.generated.resources.lose_weight_slowly_objective
import nutron.composeapp.generated.resources.low_activity_level
import nutron.composeapp.generated.resources.mantain_weight_objective
import nutron.composeapp.generated.resources.moderate_activity_level
import nutron.composeapp.generated.resources.very_high_activity_level
import org.jetbrains.compose.resources.StringResource

@Serializable
data class SetUpUiState(
    val weight: String = "",
    val height: String = "",
    val gender: String = "",
    val age: String = "",
    val activity : Activity = Activity.LOW,
    val goal: Goal = Goal.MAINTAIN,
    val formula: String = "Harris-Benedict",
    val diet: String = "standard"
)

@Serializable
enum class Activity(val factor: Double)
{
    LOW(1.2),
    MODERATE(1.375),
    HIGH(1.55),
    VERY_HIGH(1.725),
    HYPERACTIVE(1.9);

    val level: StringResource
        get() = when(this) {
            LOW -> Res.string.low_activity_level
            MODERATE -> Res.string.moderate_activity_level
            HIGH -> Res.string.high_activity_level
            VERY_HIGH -> Res.string.very_high_activity_level
            HYPERACTIVE -> Res.string.high_activity_level
        }
}

@Serializable
enum class Goal(val factor: Double) {
    LOSE_WEIGHT(0.8),
    LOSE_SLOWLY(0.9),
    MAINTAIN(1.0),
    GAIN_MUSCLE(1.10),
    GAIN_MUSCLE_SLOWLY(1.20);

    val objective: StringResource
        get() = when(this) {
            LOSE_WEIGHT -> Res.string.lose_weight_objective
            LOSE_SLOWLY -> Res.string.lose_weight_slowly_objective
            MAINTAIN -> Res.string.mantain_weight_objective
            GAIN_MUSCLE -> Res.string.gain_weight_objective
            GAIN_MUSCLE_SLOWLY -> Res.string.gain_weight_slowly_objective
        }

}