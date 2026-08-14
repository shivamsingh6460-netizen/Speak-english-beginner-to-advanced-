package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Vibrant Palette - Light ColorScheme
 * High-contrast, energetic, and engaging colors tailored for language learning:
 * - Primary: Energetic Royal Purple / Violet (#6750A4)
 * - Secondary: Vivid Flame Amber / Orange (#EA580C)
 * - Tertiary: Emerald Learning Green (#00897B)
 * - Clean, high-contrast surfaces for Hindi Devanagari & English text legibility
 */
val VibrantLightColorScheme: ColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),
    inversePrimary = Color(0xFFD0BCFF),

    secondary = Color(0xFFEA580C),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFDBCF),
    onSecondaryContainer = Color(0xFF380D00),

    tertiary = Color(0xFF00897B),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFB2DFDB),
    onTertiaryContainer = Color(0xFF004D40),

    background = Color(0xFFFDF8FF),
    onBackground = Color(0xFF1C1B1F),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFF3EDF7),
    onSurfaceVariant = Color(0xFF49454F),
    surfaceTint = Color(0xFF6750A4),

    inverseSurface = Color(0xFF313033),
    inverseOnSurface = Color(0xFFF4EFF4),

    outline = Color(0xFFCAC4D0),
    outlineVariant = Color(0xFFE7E0EC),

    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    scrim = Color(0xFF000000)
)

/**
 * Vibrant Palette - Dark ColorScheme
 * Deep saturated tones preserving visual hierarchy and high readability in low-light environments.
 */
val VibrantDarkColorScheme: ColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xFFEADDFF),
    inversePrimary = Color(0xFF6750A4),

    secondary = Color(0xFFFFB59D),
    onSecondary = Color(0xFF5E1700),
    secondaryContainer = Color(0xFF812700),
    onSecondaryContainer = Color(0xFFFFDBCF),

    tertiary = Color(0xFF80CBC4),
    onTertiary = Color(0xFF003731),
    tertiaryContainer = Color(0xFF005047),
    onTertiaryContainer = Color(0xFF9DF2E8),

    background = Color(0xFF141218),
    onBackground = Color(0xFFE6E0E9),

    surface = Color(0xFF1D1B20),
    onSurface = Color(0xFFE6E0E9),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    surfaceTint = Color(0xFFD0BCFF),

    inverseSurface = Color(0xFFE6E0E9),
    inverseOnSurface = Color(0xFF313033),

    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    scrim = Color(0xFF000000)
)

/**
 * Extended semantic tokens specifically tuned for language learning workflows,
 * tutor corrections, grammar tips, and interactive flashcard badges.
 */
@Immutable
data class VibrantExtendedColors(
    val tipBg: Color = Color(0xFFE6F4EA),
    val tipText: Color = Color(0xFF1E8E3E),
    val tipBorder: Color = Color(0x3334A853),
    val correctionBg: Color = Color(0xFFFFF8E1),
    val correctionBorder: Color = Color(0xFFFFE082),
    val correctionText: Color = Color(0xFFE65100),
    val flameAccent: Color = Color(0xFFEA580C),
    val tealAccent: Color = Color(0xFF00897B),
    val magentaAccent: Color = Color(0xFFD81B60),
    val cardGradientStart: Color = Color(0xFF6750A4),
    val cardGradientEnd: Color = Color(0xFF4F378B)
)

val LocalVibrantExtendedColors = staticCompositionLocalOf { VibrantExtendedColors() }

val MaterialTheme.vibrantColors: VibrantExtendedColors
    @Composable
    @ReadOnlyComposable
    get() = LocalVibrantExtendedColors.current

/**
 * Main application theme wrapper for SpeakEasy English learning application.
 */
@Composable
fun SpeakEasyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> VibrantDarkColorScheme
        else -> VibrantLightColorScheme
    }

    val extendedColors = if (darkTheme) {
        VibrantExtendedColors(
            tipBg = Color(0xFF1B5E20),
            tipText = Color(0xFFA5D6A7),
            tipBorder = Color(0xFF2E7D32),
            correctionBg = Color(0xFF422006),
            correctionBorder = Color(0xFF78350F),
            correctionText = Color(0xFFFEF3C7),
            flameAccent = Color(0xFFFF8A65),
            tealAccent = Color(0xFF80CBC4),
            magentaAccent = Color(0xFFF48FB1),
            cardGradientStart = Color(0xFF4F378B),
            cardGradientEnd = Color(0xFF381E72)
        )
    } else {
        VibrantExtendedColors()
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(LocalVibrantExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
