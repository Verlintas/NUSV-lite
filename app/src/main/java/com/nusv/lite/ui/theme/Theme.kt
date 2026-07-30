package com.nusv.lite.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun NusvTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeName: String = "Default (Pink)",
    content: @Composable () -> Unit
) {
    val theme = remember(themeName) {
        availableThemes.find { it.name == themeName } ?: availableThemes.first()
    }
    val colorScheme = remember(theme, darkTheme) {
        createColorScheme(theme, darkTheme)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NusvTypography,
        content = content
    )
}
