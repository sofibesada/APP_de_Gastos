package com.example.app_gastos.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppColorScheme = lightColorScheme(
    primary           = Color(0xFF6650A4),
    onPrimary         = Color.White,
    primaryContainer  = Color(0xFFEADDFF),
    onPrimaryContainer= Color(0xFF21005D),
    secondary         = Color(0xFF625B71),
    onSecondary       = Color.White,
    tertiary          = Color(0xFF7D5260),
    onTertiary        = Color.White,
    background        = Color(0xFFFFFBFE),
    onBackground      = Color(0xFF1C1B1F),
    surface           = Color(0xFFFFFBFE),
    onSurface         = Color(0xFF1C1B1F),
    error             = Color(0xFFB3261E),
    onError           = Color.White,
)

@Composable
fun APP_GastosTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography  = Typography,
        content     = content
    )
}
