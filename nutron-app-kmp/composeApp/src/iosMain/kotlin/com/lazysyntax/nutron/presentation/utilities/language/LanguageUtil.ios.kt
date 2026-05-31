package com.lazysyntax.nutron.presentation.utilities.language

// iosMain
import platform.Foundation.NSUserDefaults
import platform.Foundation.setValue

actual fun changeLanguage(lang: String) {
    NSUserDefaults.standardUserDefaults.setValue(listOf(lang), forKey = "AppleLanguages")
    // Nota: En iOS, el cambio de idioma requerir reiniciar la app
}