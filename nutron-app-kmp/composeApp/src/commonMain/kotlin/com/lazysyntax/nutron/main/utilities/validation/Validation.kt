package com.lazysyntax.nutron.main.utilities.validation

// Validacion.kt -----------------------------------------------
// Abstracción del resultado de una validación.
// Si hay error, se indica el mensaje de error.
// Será el UIState que reciben nuestros TextField para indicar si hay error o no.
// Puedo crear una unstancia de Validacion con un objeto anónimo que
// implemente la interfaz Validacion usando object : Validacion { ... }
interface Validation {
    val error: Boolean
        get() = false
    val errorMessage: String?
        get() = null
}