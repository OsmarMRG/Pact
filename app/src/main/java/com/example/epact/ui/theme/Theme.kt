package com.example.epact.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val EpactColorScheme = darkColorScheme(
    primary = PactAccent,
    onPrimary = PactBlack,
    secondary = PactOrange,
    onSecondary = PactText,
    tertiary = PactBlueSoft,
    background = PactBlack,
    onBackground = PactText,
    surface = PactSurface,
    onSurface = PactText,
    surfaceVariant = PactSurfaceAlt,
    onSurfaceVariant = PactMuted,
    outline = PactBorder,
)

@Composable
fun EpactTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EpactColorScheme,
        typography = Typography,
        content = content
    )
}
