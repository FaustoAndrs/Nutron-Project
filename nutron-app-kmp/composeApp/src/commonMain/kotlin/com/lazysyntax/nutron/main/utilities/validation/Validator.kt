package com.lazysyntax.nutron.main.utilities.validation

// Validador.kt -----------------------------------------------
/*  Abstracción de una función de validadora de datos.
*   Devuelve un objeto Validación que devolverá un estado de validación para un TextField o un Snackbar.
* */
interface Validator<T> {
    fun validate(data: T): Validation
}

