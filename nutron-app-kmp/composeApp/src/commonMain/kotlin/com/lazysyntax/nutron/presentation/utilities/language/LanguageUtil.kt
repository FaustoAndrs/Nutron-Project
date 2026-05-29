package com.lazysyntax.nutron.presentation.utilities.language

/**
 * **LANGUAGE:** Función "multiplataforma" para cambiar el idioma de la aplicación.
 * Requiere implementaciones específicas en Android e iOS para actualizar el locale del sistema.
 */
expect fun changeLanguage(lang: String)