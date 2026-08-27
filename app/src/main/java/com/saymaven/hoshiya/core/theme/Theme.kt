package com.saymaven.hoshiya.core.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.saymaven.hoshiya.core.model.AppThemePalette

fun createColorScheme(palette: AppThemePalette): ColorScheme {
    val primary = palette.primaryColor
    return darkColorScheme(
        primary = primary,
        onPrimary = SpaceBlack,
        primaryContainer = SurfaceDarkElevated,
        onPrimaryContainer = TextPrimary,
        secondary = SakuraPink,
        onSecondary = SpaceBlack,
        secondaryContainer = SurfaceDark,
        onSecondaryContainer = TextPrimary,
        tertiary = StarlightAmber,
        onTertiary = SpaceBlack,
        background = SpaceBlack,
        onBackground = TextPrimary,
        surface = SurfaceDark,
        onSurface = TextPrimary,
        surfaceVariant = SurfaceDarkElevated,
        onSurfaceVariant = TextSecondary,
        outline = SurfaceBorder
    )
}

@Composable
fun HoshiyaTheme(
    palette: AppThemePalette = AppThemePalette.CELESTIAL,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            dynamicDarkColorScheme(context)
        }
        else -> createColorScheme(palette)
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
