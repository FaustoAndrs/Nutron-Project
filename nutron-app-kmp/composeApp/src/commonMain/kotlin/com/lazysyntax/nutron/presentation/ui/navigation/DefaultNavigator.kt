package com.lazysyntax.nutron.presentation.ui.navigation

import androidx.compose.runtime.mutableStateListOf

/**
 * **NAVIGATION:** Implementación concreta del Navigator.
 * Utiliza un mutableStateListOf para que los cambios en el backstack sean reactivos en Compose.
 */
class DefaultNavigator(initialRoute: Route) : Navigator {
    // Lista observable que representa la pila de pantallas actual.
    private val _backstack = mutableStateListOf(initialRoute)
    override val backstack: List<Route> get() = _backstack

    // Añade una nueva ruta al backstack.
    override fun navigateTo(route: Route) {
        _backstack.add(route)
    }

    // Elimina la ruta actual y vuelve  a la anterior.
    override fun goBack() {
        if (_backstack.size > 1) {
            _backstack.removeAt(_backstack.size - 1)
        }
    }

    // Limpia todo el stack de rutas y establece una nueva ruta raíz.
    override fun resetTo(route: Route){
        _backstack.clear()
        _backstack.add(route)
    }
}