package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CoralRed,
    onPrimary = Color.White,
    primaryContainer = DarkBtnOpBg,
    onPrimaryContainer = LightCoral,
    secondary = LightCoral,
    onSecondary = Color.White,
    background = Color(0xFF090D16),
    onBackground = DarkTextPrimary,
    surface = DarkCardBg,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkBtnBg,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = CoralRed,
    onPrimary = Color.White,
    primaryContainer = LightBtnOpBg,
    onPrimaryContainer = CoralRed,
    secondary = CoralRed,
    onSecondary = Color.White,
    background = Color(0xFFF1F5F9),
    onBackground = LightTextPrimary,
    surface = LightCardBg,
    onSurface = LightTextPrimary,
    surfaceVariant = LightBtnBg,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
