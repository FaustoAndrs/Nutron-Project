package com.lazysyntax.nutron.main.utilities.language

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

actual fun changeLanguage(lang: String) {
    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(lang)
    AppCompatDelegate.setApplicationLocales(appLocale)
}