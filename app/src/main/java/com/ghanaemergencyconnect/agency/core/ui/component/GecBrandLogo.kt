package com.ghanaemergencyconnect.agency.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ghanaemergencyconnect.agency.R

/**
 * Renders the approved Ghana Emergency Connect brand mark shared across the
 * citizen and staff applications. The artwork itself remains unchanged;
 * sizing and surrounding surfaces are controlled by the host screen.
 */
@Composable
fun GecBrandLogo(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .padding(4.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.gec_icon_transparent),
            contentDescription = "Ghana Emergency Connect Shield Logo",
            modifier = Modifier.fillMaxSize()
        )
    }
}
