package com.dino.app.ui.theme

import androidx.compose.material3.MaterialTheme
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
    surface = Color.White, onSurface = TextDark
)

@Composable
fun PaleoTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColors, content = content)
}