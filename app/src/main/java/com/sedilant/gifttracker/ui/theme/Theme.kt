package com.sedilant.gifttracker.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = RedPrimary,
    onPrimary = Color.White,
    primaryContainer = RedSecondary,
    onPrimaryContainer = Color.White,

    secondary = ChartGreen,
    onSecondary = Color.White,
    secondaryContainer = GreenBackground,
    onSecondaryContainer = ChartGreen,

    tertiary = ChartYellow,
    onTertiary = TextPrimary,
    tertiaryContainer = YellowBackground,
    onTertiaryContainer = ChartYellow,

    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),

    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),

    outline = DividerGray,
    outlineVariant = Color(0xFF49454F)
)

private val LightColorScheme = lightColorScheme(
    primary = RedPrimary,
    onPrimary = Color.White,
    primaryContainer = RedLight,
    onPrimaryContainer = RedPrimary,

    secondary = ChartGreen,
    onSecondary = Color.White,
    secondaryContainer = GreenBackground,
    onSecondaryContainer = ChartGreen,

    tertiary = ChartYellow,
    onTertiary = TextPrimary,
    tertiaryContainer = YellowBackground,
    onTertiaryContainer = ChartYellow,

    background = BackgroundLight,
    onBackground = TextPrimary,

    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundLight,
    onSurfaceVariant = TextSecondary,

    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    outline = DividerGray,
    outlineVariant = Color(0xFFCAC4D0)
)

@Composable
fun GiftTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Cambiado a false para usar colores personalizados
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}