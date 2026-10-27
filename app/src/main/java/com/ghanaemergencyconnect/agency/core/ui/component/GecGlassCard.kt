package com.ghanaemergencyconnect.agency.core.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Provides the shared translucent surface treatment used for high-level
 * operational content. The component intentionally avoids requiring
 * platform blur so the visual language remains consistent on Android 8+
 * devices supported by GEC.
 */
@Composable
fun GecGlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    containerColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    borderColor: Color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
    elevation: Dp = 2.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val border = BorderStroke(width = 1.dp, color = borderColor)
    val colors = CardDefaults.cardColors(
        containerColor = containerColor,
        contentColor = contentColor
    )

    if (onClick != null) {
        Card(
            onClick = onClick,
            shape = shape,
            border = border,
            colors = colors,
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
            modifier = modifier
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    } else {
        Card(
            shape = shape,
            border = border,
            colors = colors,
            elevation = CardDefaults.cardElevation(defaultElevation = elevation),
            modifier = modifier
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}
