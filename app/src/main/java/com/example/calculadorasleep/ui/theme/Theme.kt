package com.example.calculadorasleep.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CitrusZest,
    secondary = SeaBreeze,
    tertiary = AmalfiTile,
    background = Color(0xFF1A1A1A),
    surface = Color(0xFF242424),
    onPrimary = TextDark,
    onBackground = CreamGelato,
    onSurface = CreamGelato,
    onSurfaceVariant = CreamGelato
)

private val LightColorScheme = lightColorScheme(
    primary = CitrusZest,
    onPrimary = TextDark,

    secondary = SeaBreeze,
    onSecondary = TextDark,

    tertiary = AmalfiTile,
    onTertiary = TextLight,

    background = SkyMist,
    onBackground = TextDark,

    surface = SkyMist,
    onSurface = TextDark,

    surfaceVariant = Color.White,
    onSurfaceVariant = TextDark,
    outline = OutlineColor
)

@Composable
fun CalculadoraSleepTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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