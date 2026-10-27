package com.ghanaemergencyconnect.agency.feature.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ghanaemergencyconnect.agency.core.ui.component.GecGlassCard
import com.ghanaemergencyconnect.agency.core.ui.component.OperationalScopeCard
import com.ghanaemergencyconnect.agency.data.domain.model.AuthenticatedStaff

/**
 * System Administrator access on this mobile client remains oversight-oriented
 * and does not inherit agency operational permissions.
 */
@Composable
fun SystemAdminDashboard(
    staff: AuthenticatedStaff,
    onNavigateToNotifications: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OperationalScopeCard(staff = staff)

        Spacer(modifier = Modifier.height(16.dp))

        GecGlassCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "National System Oversight View",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Operational agency mutations are restricted on mobile devices.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "System Options",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickActionCard(
            title = "System Alerts",
            subtitle = "Review system-wide alerts and dispatches",
            icon = Icons.Default.Notifications,
            onClick = onNavigateToNotifications
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickActionCard(
            title = "Administrator Profile",
            subtitle = "View system administrator account details",
            icon = Icons.Default.Person,
            onClick = onNavigateToProfile
        )
    }
}
