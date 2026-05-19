package com.lazysyntax.nutron.presentation.ui.features.setUp

import com.lazysyntax.nutron.presentation.utilities.validation.Validation
import com.lazysyntax.nutron.presentation.utilities.validation.ComposedValidation
import org.jetbrains.compose.resources.StringResource

data class SetUpUiStateValidation(
    val weightValidation: Validation = object : Validation {},
    val heightValidation: Validation = object : Validation {},
    val ageValidation: Validation = object : Validation {},
    val genderValidation: Validation = object : Validation {},
    val activityValidation: Validation = object : Validation {},
    val goalValidation: Validation = object : Validation {},
    val formulaValidation: Validation = object : Validation {},
) : Validation {
    private var validacionCompuesta: ComposedValidation? = null

    private fun componerValidacion(): ComposedValidation {
        val composed = ComposedValidation()
            .add(weightValidation)
            .add(heightValidation)
            .add(ageValidation)
            .add(genderValidation)
            .add(activityValidation)
            .add(goalValidation)
            .add(formulaValidation)
        validacionCompuesta = composed
        return composed
    }

    override val error: Boolean
        get() = validacionCompuesta?.error ?: componerValidacion().error

    override val errorMessage: StringResource?
        get() = validacionCompuesta?.errorMessage ?: componerValidacion().errorMessage
}
