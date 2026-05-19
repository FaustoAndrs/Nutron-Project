package com.lazysyntax.nutron.presentation.ui.navigation

interface Navigator {
    val backstack: List<Route>
    fun navigateTo(route: Route)
    fun goBack()
    fun resetTo(route: Route)
}