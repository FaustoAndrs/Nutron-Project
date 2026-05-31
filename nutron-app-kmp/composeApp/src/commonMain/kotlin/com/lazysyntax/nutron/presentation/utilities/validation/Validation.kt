package com.lazysyntax.nutron.presentation.utilities.validation

import org.jetbrains.compose.resources.StringResource


interface Validation {
    val error: Boolean
        get() = false
    val errorMessage: StringResource?
        get() = null
}