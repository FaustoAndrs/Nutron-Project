package com.lazysyntax.nutron.presentation.ui.navigation

import androidx.compose.runtime.mutableStateListOf

class DefaultNavigator(initialRoute: Route) : Navigator {
    // Usamos internal para que solo el Navigator pueda mutar, pero sea observable
    private val _backstack = mutableStateListOf(initialRoute)
    override val backstack: List<Route> get() = _backstack

    override fun navigateTo(route: Route) {
        _backstack.add(route)
    }

    override fun goBack() {
        //_backstack.removeLast()
        if (_backstack.size > 1) {
            _backstack.removeAt(_backstack.size - 1)
        }
    }

    override fun resetTo(route: Route){
        _backstack.clear()
        _backstack.add(route)
    }
}