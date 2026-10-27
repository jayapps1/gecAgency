package com.ghanaemergencyconnect.agency.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = EmergencyBlueLight,
    onPrimary = NavyOnPrimary,
    primaryContainer = NavyPrimary,
    onPrimaryContainer = NavyContainer,
    secondary = GhanaGold,
    onSecondary = NavyOnPrimary,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    error = GhanaRed,
    errorContainer = GhanaRedContainer,
    onErrorContainer = GhanaOnRedContainer
)

private val LightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = NavyOnPrimary,
    primaryContainer = NavyContainer,
    onPrimaryContainer = NavyOnContainer,
    secondary = EmergencyBlue,
    onSecondary = EmergencyOnBlue,
    secondaryContainer = EmergencyBlueContainer,
    onSecondaryContainer = NavyOnContainer,
    surface = SurfaceNeutral,
    onSurface = OnSurfaceNeutral,
    surfaceVariant = SurfaceVariantNeutral,
    onSurfaceVariant = OnSurfaceVariantNeutral,
    error = GhanaRed,
    errorContainer = GhanaRedContainer,
    onErrorContainer = GhanaOnRedContainer
)

/**
 * Defines the staff application's operational visual system.
 * Semantic status colors remain separate from brand colors so emergency
 * readiness states are never inferred from decorative styling.
 */
@Composable
fun GhanaEmergencyConnectAgencyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
