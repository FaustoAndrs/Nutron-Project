package com.lazysyntax.nutron.main.ui.features.diary.macros

import androidx.lifecycle.ViewModel
import com.lazysyntax.nutron.models.Nutriments
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MacrosViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(MacrosUiState())
    val uiState: StateFlow<MacrosUiState> = _uiState.asStateFlow()

    fun onQuantityFieldChange(value: Double) {

        updateNutriments { copy(proteins = proteins) }
    }



    /**
     * Añade el alimento actual a la lista de alimentos de la comida.
     */
    fun addFood() {
        _uiState.update { state ->
            val foodToAdd = state.food ?: return@update state
            
            state.copy(
                meals = state.meals?.let { meal ->
                    meal.copy(
                        foods = (meal.foods ?: emptyList()) + foodToAdd
                    )
                }
            )
        }
    }

    /**
     * Función de utilidad para actualizar campos dentro de Nutriments de forma segura.
     */
    private fun updateNutriments(updateBlock: Nutriments.() -> Nutriments) {
        _uiState.update { state ->
            state.copy(
                food = state.food?.let { food ->
                    food.copy(
                        nutriments = (food.nutriments ?: Nutriments()).updateBlock()
                    )
                }
            )
        }
    }
}
