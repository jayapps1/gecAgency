package com.ghanaemergencyconnect.agency.feature.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ghanaemergencyconnect.agency.core.ui.component.GecGlassCard
import com.ghanaemergencyconnect.agency.core.ui.component.OperationalScopeCard
import com.ghanaemergencyconnect.agency.data.domain.model.AuthenticatedStaff

/**
 * Responder navigation intentionally excludes administrative management functions.
 */
@Composable
fun ResponderDashboard(
    staff: AuthenticatedStaff,
    onNavigateToIncidents: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToAppliances: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OperationalScopeCard(staff = staff)

        Spacer(modifier = Modifier.height(16.dp))

        GecGlassCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Responder Readiness Active",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Operational workspace active. Emergency response status monitored.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Responder Quick Actions",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickActionCard(
            title = "Active Assigned Incidents",
            subtitle = "View assigned incidents and location maps",
            icon = Icons.Default.ReportProblem,
            onClick = onNavigateToIncidents
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickActionCard(
            title = "Dispatch Alerts",
            subtitle = "Review incoming dispatch notifications",
            icon = Icons.Default.Notifications,
            onClick = onNavigateToNotifications
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickActionCard(
            title = "Station Fleet Availability",
            subtitle = "Check station appliance status",
            icon = Icons.Default.DirectionsBus,
            onClick = onNavigateToAppliances
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickActionCard(
            title = "Responder Profile",
            subtitle = "View assigned station and responder credentials",
            icon = Icons.Default.Person,
            onClick = onNavigateToProfile
        )
    }
}
