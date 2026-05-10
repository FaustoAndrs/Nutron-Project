package com.lazysyntax.nutron.main.ui.features.setUp


import com.lazysyntax.nutron.main.ui.features.targets.composables.Diet
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
import org.jetbrains.compose.resources.stringResource

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

enum class Activity(val level: StringResource, val factor: Double)
{
    LOW(Res.string.low_activity_level, 1.2),
    MODERATE(Res.string.moderate_activity_level, 1.375),
    HIGH(Res.string.high_activity_level, 1.55),
    VERY_HIGH(Res.string.very_high_activity_level, 1.725),
    HYPERACTIVE(Res.string.high_activity_level, 1.9)
}
enum class Goal(val objective: StringResource, val factor: Double) {
    LOSE_WEIGHT(Res.string.lose_weight_objective, 0.8),
    LOSE_SLOWLY(Res.string.lose_weight_slowly_objective, 0.9),
    MAINTAIN(Res.string.mantain_weight_objective, 1.0),
    GAIN_MUSCLE(Res.string.gain_weight_objective, 1.10),
    GAIN_MUSCLE_SLOWLY(Res.string.gain_weight_slowly_objective, 1.20)

}