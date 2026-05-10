package com.lazysyntax.nutron.main.utilities.language

// iosMain
import platform.Foundation.NSUserDefaults
import platform.Foundation.setValue

actual fun changeLanguage(lang: String) {
    NSUserDefaults.standardUserDefaults.setValue(listOf(lang), forKey = "AppleLanguages")
    // Nota: En iOS, los cambios de AppleLanguages suelen requerir reiniciar la app
    // o manejar la reactividad manualmente en el root.
}