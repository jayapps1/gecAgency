package com.ghanaemergencyconnect.agency.core.ui.component

import androidx.compose.runtime.Composable

@Composable
fun AccessDeniedScreen(
    onBackClick: () -> Unit
) {
    PlaceholderScreen(
        title = "Access Denied",
        message = "You do not have permission to access this area on this device.",
        onBackClick = onBackClick
    )
}
