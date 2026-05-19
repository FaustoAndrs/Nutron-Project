package com.lazysyntax.nutron.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Basic-Fit Brand Colors
val BasicFitOrange = Color(0xFFFF7000)
val BasicFitBlack = Color(0xFF1D1D1B)
val BasicFitWhite = Color(0xFFFFFFFF)
val BasicFitLightGrey = Color(0xFFF2F2F2)
val BasicFitDarkGrey = Color(0xFF2C2C2C)

// Light Theme Colors
internal val LightColorScheme = lightColorScheme(
    tertiary = BasicFitDarkGrey,
    primary = BasicFitOrange,
    onPrimary = BasicFitWhite,
    primaryContainer = BasicFitOrange.copy(alpha = 0.1f),
    onPrimaryContainer = BasicFitOrange,
    secondary = BasicFitBlack,
    onSecondary = BasicFitWhite,
    background = BasicFitWhite,
    onBackground = BasicFitBlack,
    surface = BasicFitLightGrey,
    onSurface = BasicFitBlack,
    error = Color(0xFFB00020),
    onError = BasicFitWhite
)

// Dark Theme Colors
internal val DarkColorScheme = darkColorScheme(
    primary = BasicFitOrange,
    onPrimary = BasicFitWhite,
    primaryContainer = BasicFitOrange.copy(alpha = 0.2f),
    onPrimaryContainer = BasicFitOrange,
    secondary = BasicFitWhite,
    onSecondary = BasicFitBlack,
    background = BasicFitBlack,
    onBackground = BasicFitWhite,
    surface = BasicFitDarkGrey,
    onSurface = BasicFitWhite,
    error = Color(0xFFCF6679),
    onError = BasicFitBlack
)
