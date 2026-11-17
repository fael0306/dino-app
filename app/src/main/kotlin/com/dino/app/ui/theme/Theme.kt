package com.dino.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF1A2A3A)
val PrimaryLight = Color(0xFF2C4A5A)
val Secondary = Color(0xFFD4A373)
val Accent = Color(0xFFE76F51)
val Bg = Color(0xFFF4F1EA)
val TextDark = Color(0xFF1E2A36)

private val LightColors = lightColorScheme(
    primary = Primary, onPrimary = Color.White,
    primaryContainer = PrimaryLight, onPrimaryContainer = Color.White,
    secondary = Secondary, onSecondary = Color.White,
    tertiary = Accent,
    background = Bg, onBackground = TextDark,
    surface = Color.White, onSurface = TextDark,
    surfaceVariant = Color(0xFFEEEEEE)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9CC5DD), onPrimary = Color(0xFF0F1A24),
    primaryContainer = PrimaryLight, onPrimaryContainer = Color.White,
    secondary = Secondary, onSecondary = Color(0xFF1E1408),
    tertiary = Accent,
    background = Color(0xFF121A22), onBackground = Color(0xFFE6E1D8),
    surface = Color(0xFF1B2630), onSurface = Color(0xFFE6E1D8),
    surfaceVariant = Color(0xFF2A3742), onSurfaceVariant = Color(0xFFC9D1D8)
)

@Composable
fun PaleoTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) DarkColors else LightColors, content = content)
}
