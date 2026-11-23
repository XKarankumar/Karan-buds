package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.model.ThemeMode

@Composable
fun KaranBudsTheme(
    themeMode: ThemeMode = ThemeMode.AMOLED,
    accentColorIndex: Int = 0,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        ThemeMode.AMOLED -> true
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.AUTO -> systemDark
    }

    val accentColor = KaranAccentColors.getOrElse(accentColorIndex) { KaranPrimary }

    val colorScheme = if (isDark) {
        darkColorScheme(
            primary = accentColor,
            secondary = KaranSecondary,
            tertiary = KaranTertiary,
            background = if (themeMode == ThemeMode.AMOLED) Color(0xFF000000) else KaranBackgroundDark,
            surface = if (themeMode == ThemeMode.AMOLED) Color(0xFF0A0A0A) else KaranSurfaceDark,
            onBackground = Color.White,
            onSurface = Color.White
        )
    } else {
        lightColorScheme(
            primary = accentColor,
            secondary = KaranSecondary,
            tertiary = KaranTertiary,
            background = Color(0xFFF5F7FA),
            surface = Color(0xFFFFFFFF),
            onBackground = Color(0xFF111827),
            onSurface = Color(0xFF111827)
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
