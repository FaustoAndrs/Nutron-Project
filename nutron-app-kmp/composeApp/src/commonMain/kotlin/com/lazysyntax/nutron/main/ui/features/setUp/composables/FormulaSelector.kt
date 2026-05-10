package com.lazysyntax.nutron.main.ui.features.setUp.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.main.ui.features.setUp.Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BMRSelector(
    sheetState: SheetState,
    scope: CoroutineScope,
    onFormulaSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {

    ModalBottomSheet(
        onDismissRequest = { onDismiss() }, sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp, top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Selecciona tu objetivo",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            MetabolicFormula.entries.forEach { equation ->
                FormulaOption(equation.label) {
                    onFormulaSelected(equation.label)
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        onDismiss()
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IMMMSelector(
    sheetState: SheetState,
    scope: CoroutineScope,
    onFormulaSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val equations = listOf(
        "Boer",
        "James",
    )

    ModalBottomSheet(
        onDismissRequest = { onDismiss() }, sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp, top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Selecciona tu objetivo",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            equations.forEach { equation ->
                FormulaOption(equation) {
                    onFormulaSelected(equation)
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        onDismiss()
                    }
                }
            }
        }
    }
}
@Composable
fun FormulaOption(label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
    }
}

enum class MetabolicFormula(val label: String) {
    HARRIS_BENEDICT("Harris-Benedict"), MIFFLIN_ST_JEOR("Mifflin-St Jeor"), KATCH_MCARDLE("Katch-McArdle");

    companion object {
        fun fromLabel(label: String) = entries.find { it.label == label } ?: MIFFLIN_ST_JEOR
    }
}
enum class LineMuscularMassFormula(val label: String) {
    BOER("Boer"), JAMES("James");

    companion object {
        fun fromLabel(label: String) = entries.find { it.label == label } ?: BOER
    }
}
object Calculator {
    // Ratio Metabolismo Basal
    fun calculateBMR(
        weight: Double,
        height: Double,
        age: Int,
        gender: String,
        fatPercentage: Double = 0.0,
        formulaLabel: String
    ): Double {
        val formula = MetabolicFormula.fromLabel(formulaLabel)
        return when (formula) {
            MetabolicFormula.HARRIS_BENEDICT -> {
                if (gender.lowercase() == "male") {
                    66.47 + (13.75 * weight) + (5.003 * height) - (6.755 * age)
                } else {
                    655.1 + (9.563 * weight) + (1.85 * height) - (4.676 * age)
                }
            }

            MetabolicFormula.MIFFLIN_ST_JEOR -> {
                val s = if (gender.lowercase() == "male") 5 else -161
                (10 * weight) + (6.25 * height) - (5 * age) + s
            }

            MetabolicFormula.KATCH_MCARDLE -> {
                // Esta requiere porcentaje de grasa, si no está definida, se devuelve Mifflin-St-Jeor
                if (fatPercentage == 0.0) {
                    370 + (21.6 * (fatPercentage * weight))
                } else {
                    (10 * weight) + (6.25 * height) - (5 * age)
                }
            }
        }
    }

    fun calculateIMMM(
        weight: Double,
        height: Double,
        gender: String,
        formulaLabel: String
    ): Double {
        val formula = LineMuscularMassFormula.fromLabel(formulaLabel)
        return when (formula) {
            LineMuscularMassFormula.BOER -> {
                if (gender.lowercase() == "male") {
                    (1.10 * weight) - 128 * (weight / height).pow(2)
                } else {
                    (1.07 * weight) - 148 * (weight / height).pow(2)
                }

            }

            LineMuscularMassFormula.JAMES -> {
                if (gender.lowercase() == "male") {
                    (0.407 * weight) + (0.267 * height) - 19.2//height en Cm
                } else {
                    (0.252 * weight) + (0.473 * height) - 48.3
                }
            }
        }
    }


    // Total agua corporal
    fun calculateTBW(
        weight: Double,
        height: Double,
        age: Int,
        gender: String,
    ): Double {
        return if (gender.lowercase() == "male") {
            66.47 + (13.75 * weight) + (5.003 * height) - (6.755 * age)
        } else {
            655.1 + (9.563 * weight) + (1.85 * height) - (4.676 * age)
        }
    }

    /* Índice de masa corporal / Body mass index (BMI)
        Bajo peso: < 18.5
        Peso saludable: 18.5 - 24.9
        Sobrepeso: 25.0 - 29.9
        Obesidad: 30.0 o más
    */
    fun calculateBMI(weight: Double, heightCm: Double): Double {
        val height = heightCm / 100
        return weight / (height.pow(2))
    }


    fun calculateMusclePercentage(
        leanBodyMassKg: Double,
        totalWeightKg: Double
    ): Double {
        return (leanBodyMassKg / totalWeightKg) * 100
    }

    fun calculateFatPercentage(
        bmi: Double,
        age: Int,
        gender: String
    ): Double {
        val genderFactor = if (gender.lowercase() == "male") 1.0 else 0.0
        return (1.20 * bmi) + (0.23 * age) - (10.8 * genderFactor) - 5.4
    }

    fun calculateGET(
        bmr: Double,
        activityFactor: Double
    ): Double {
        return bmr * activityFactor
    }

    fun calculateEB(value: Double, goal: String): Double {
        return when(goal){
            "Lose weight" -> value * 0.8
            "Gain weight" -> value * 1.2
            "Maintain weight" -> value
            else -> value
        }


    }

}