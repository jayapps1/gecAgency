package com.ghanaemergencyconnect.agency.core.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ghanaemergencyconnect.agency.R
import com.ghanaemergencyconnect.agency.core.navigation.AppDestination

/**
 * Provides the primary role-aware navigation surface using a translucent
 * floating treatment inspired by modern mobile system interfaces. The
 * component degrades gracefully on Android versions that do not support
 * platform background blur.
 */
@Composable
fun GecBottomNavigation(
    currentDestination: String?,
    destinations: List<AppDestination>,
    unreadCount: Int = 0,
    onDestinationSelected: (AppDestination) -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val glassColor = if (isDark) Color(0xFF071525).copy(alpha = 0.82f) else Color.White.copy(alpha = 0.78f)
    val glassBorderColor = if (isDark) Color.White.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.35f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = glassColor,
            border = BorderStroke(1.dp, glassBorderColor),
            shadowElevation = 10.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                destinations.forEach { destination ->
                    val selected = currentDestination == destination.route
                    val icon = when (destination) {
                        AppDestination.Dashboard -> Icons.Default.Dashboard
                        AppDestination.Incidents -> Icons.Default.ReportProblem
                        AppDestination.Notifications -> Icons.Default.Notifications
                        AppDestination.Profile -> Icons.Default.Person
                        AppDestination.Settings -> Icons.Default.Settings
                        else -> Icons.Default.Dashboard
                    }
                    val label = when (destination) {
                        AppDestination.Dashboard -> stringResource(R.string.nav_dashboard)
                        AppDestination.Incidents -> stringResource(R.string.nav_incidents)
                        AppDestination.Notifications -> stringResource(R.string.nav_notifications)
                        AppDestination.Profile -> stringResource(R.string.nav_profile)
                        AppDestination.Settings -> stringResource(R.string.nav_settings)
                        else -> ""
                    }

                    val pillAlpha by animateFloatAsState(
                        targetValue = if (selected) 0.12f else 0.0f,
                        animationSpec = tween(durationMillis = 200),
                        label = "pillAlpha"
                    )
                    val iconScale by animateFloatAsState(
                        targetValue = if (selected) 1.05f else 1.0f,
                        animationSpec = tween(durationMillis = 200),
                        label = "iconScale"
                    )
                    val contentColor by animateColorAsState(
                        targetValue = if (selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        animationSpec = tween(durationMillis = 200),
                        label = "contentColor"
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onDestinationSelected(destination) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = pillAlpha))
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        ) {
                            if (destination == AppDestination.Notifications && unreadCount > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge(
                                            containerColor = MaterialTheme.colorScheme.error,
                                            contentColor = MaterialTheme.colorScheme.onError
                                        ) {
                                            val badgeText = if (unreadCount > 99) "99+" else unreadCount.toString()
                                            Text(badgeText, style = MaterialTheme.typography.labelSmall)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = label,
                                        tint = contentColor,
                                        modifier = Modifier
                                            .size(22.dp)
                                            .scale(iconScale)
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = contentColor,
                                    modifier = Modifier
                                        .size(22.dp)
                                        .scale(iconScale)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = contentColor,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
