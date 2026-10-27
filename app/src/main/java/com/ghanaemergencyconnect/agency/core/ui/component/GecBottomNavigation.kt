package com.ghanaemergencyconnect.agency.core.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ghanaemergencyconnect.agency.R
import com.ghanaemergencyconnect.agency.core.navigation.AppDestination

/**
 * Displays only destinations available to the authenticated staff role.
 * Floating navigation container provides a modern operational shell.
 */
@Composable
fun GecBottomNavigation(
    currentDestination: String?,
    destinations: List<AppDestination>,
    onDestinationSelected: (AppDestination) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
        tonalElevation = 8.dp,
        shadowElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.0f),
            tonalElevation = 0.dp
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

                NavigationBarItem(
                    selected = selected,
                    onClick = { onDestinationSelected(destination) },
                    icon = { Icon(icon, contentDescription = label) },
                    label = { Text(label, style = MaterialTheme.typography.labelMedium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }
    }
}
