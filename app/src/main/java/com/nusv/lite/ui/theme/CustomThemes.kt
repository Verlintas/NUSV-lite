package com.nusv.lite.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

data class ThemeColors(
    val name: String,
    val lightPrimary: Color,
    val darkPrimary: Color,
    val lightBg: Color = Color(0xFFF8F8F8),
    val darkBg: Color = Color(0xFF000000),
    val lightSurface: Color = Color(0xFFFFFFFF),
    val darkSurface: Color = Color(0xFF0A0A0A),
)

val availableThemes = listOf(
    ThemeColors("Default (Pink)", Color(0xFF7C3AED), Color(0xFFFF2D78)),
    ThemeColors("Ocean", Color(0xFF0288D1), Color(0xFF4FC3F7)),
    ThemeColors("Forest", Color(0xFF2E7D32), Color(0xFF81C784)),
    ThemeColors("Sunset", Color(0xFFE65100), Color(0xFFFF8A65)),
    ThemeColors("Midnight", Color(0xFF37474F), Color(0xFF90A4AE)),
    ThemeColors("Sakura", Color(0xFFAD1457), Color(0xFFF48FB1)),
    ThemeColors("Lavender", Color(0xFF6A1B9A), Color(0xFFCE93D8)),
    ThemeColors("Gold", Color(0xFFF57F17), Color(0xFFFFD54F)),
    ThemeColors("Teal", Color(0xFF00695C), Color(0xFF80CBC4)),
    ThemeColors("Rose", Color(0xFF880E4F), Color(0xFFF48FB1)),
    ThemeColors("Mint", Color(0xFF00BFA5), Color(0xFF6FFFB0)),
    ThemeColors("Berry", Color(0xFFC2185B), Color(0xFFF06292)),
    ThemeColors("Crimson", Color(0xFFB71C1C), Color(0xFFEF5350)),
    ThemeColors("Amber", Color(0xFFFF8F00), Color(0xFFFFCA28)),
    ThemeColors("Violet", Color(0xFF7E57C2), Color(0xFFB39DDB)),
    ThemeColors("Coffee", Color(0xFF5D4037), Color(0xFFA1887F)),
    ThemeColors("Charcoal", Color(0xFF424242), Color(0xFF9E9E9E)),
    ThemeColors("Pistachio", Color(0xFF689F38), Color(0xFFAED581)),
    ThemeColors("Coral", Color(0xFFFF5722), Color(0xFFFF8A65)),
    ThemeColors("Navy", Color(0xFF1A237E), Color(0xFF7986CB)),
    ThemeColors("Peach", Color(0xFFF4511E), Color(0xFFFFAB91)),
    ThemeColors("Steel", Color(0xFF455A64), Color(0xFFB0BEC5)),
    ThemeColors("Orca", Color.White, Color.White,
        lightBg = Color.Black, darkBg = Color.Black,
        lightSurface = Color.Black, darkSurface = Color.Black),
)

fun createColorScheme(theme: ThemeColors, dark: Boolean): ColorScheme {
    if (theme.name == "Orca") {
        val orca = darkColorScheme(
            background = Color.Black,
            surface = Color.Black,
            surfaceVariant = Color.Black,
            onBackground = Color.White,
            onSurface = Color.White,
            onSurfaceVariant = Color.White,
            primary = Color.White,
            outline = Color(0x33FFFFFF),
        )
        return if (dark) orca else orca
    }
    return if (dark)
        darkColorScheme(
            background = theme.darkBg,
            surface = theme.darkSurface,
            surfaceVariant = Color(0xFF141414),
            onBackground = Color(0xFFF5F5F5),
            onSurface = Color(0xFFF5F5F5),
            onSurfaceVariant = Color(0xFF7A7A7A),
            primary = theme.darkPrimary,
            outline = Color(0xFF1F1F1F),
        )
    else
        lightColorScheme(
            background = theme.lightBg,
            surface = theme.lightSurface,
            surfaceVariant = Color(0xFFF0F0F0),
            onBackground = Color(0xFF1A1A1A),
            onSurface = Color(0xFF1A1A1A),
            onSurfaceVariant = Color(0xFF8A8A8A),
            primary = theme.lightPrimary,
            outline = Color(0xFFE8E8E8),
        )
}
