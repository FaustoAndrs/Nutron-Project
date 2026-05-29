package com.lazysyntax.nutron.presentation.ui.navigation

/**
 * **NAVIGATION:** Interfaz del orquestador de navegación.
 * Permite desacoplar la lógica de navegación de los componentes de UI.
 */
interface Navigator {
    val backstack: List<Route>
    fun navigateTo(route: Route)
    fun goBack()
    fun resetTo(route: Route)
}