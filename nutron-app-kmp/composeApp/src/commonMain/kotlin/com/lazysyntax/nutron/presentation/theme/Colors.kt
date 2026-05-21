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
    primary = BasicFitOrange,
    onPrimary = BasicFitWhite,
    primaryContainer = BasicFitOrange.copy(alpha = 0.1f),
    onPrimaryContainer = BasicFitOrange,
    secondary = BasicFitBlack,
    onSecondary = BasicFitWhite,
    secondaryContainer = BasicFitOrange.copy(alpha = 0.2f),
    onSecondaryContainer = BasicFitOrange,
    tertiary = BasicFitDarkGrey,
    onTertiary = BasicFitWhite,
    background = BasicFitWhite,
    onBackground = BasicFitBlack,
    surface = BasicFitLightGrey,
    onSurface = BasicFitBlack,
    surfaceVariant = Color(0xFFE1E1E1),
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),
    error = Color(0xFFB00020),
    onError = BasicFitWhite,
)

// Dark Theme Colors
internal val DarkColorScheme = darkColorScheme(
    primary = BasicFitOrange,
    onPrimary = BasicFitWhite,
    primaryContainer = BasicFitOrange.copy(alpha = 0.2f),
    onPrimaryContainer = BasicFitOrange,
    secondary = BasicFitWhite,
    onSecondary = BasicFitBlack,
    secondaryContainer = Color(0xFF3D3D3D),
    onSecondaryContainer = BasicFitWhite,
    tertiary = BasicFitLightGrey,
    onTertiary = BasicFitBlack,
    background = BasicFitBlack,
    onBackground = BasicFitWhite,
    surface = BasicFitDarkGrey,
    onSurface = BasicFitWhite,
    surfaceVariant = Color(0xFF3F3F3F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF44474E),
    error = Color(0xFFCF6679),
    onError = BasicFitBlack
)
