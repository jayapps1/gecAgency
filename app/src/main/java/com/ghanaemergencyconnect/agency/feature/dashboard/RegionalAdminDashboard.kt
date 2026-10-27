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
import com.ghanaemergencyconnect.agency.core.ui.component.OperationalScopeCard
import com.ghanaemergencyconnect.agency.data.domain.model.AuthenticatedStaff

@Composable
fun RegionalAdminDashboard(
    staff: AuthenticatedStaff,
    onNavigateToIncidents: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToAppliances: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OperationalScopeCard(staff = staff)

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Regional Quick Actions",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickActionCard(
            title = "Regional Incidents",
            subtitle = "Monitor active assignments within region",
            icon = Icons.Default.ReportProblem,
            onClick = onNavigateToIncidents
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickActionCard(
            title = "Regional Alerts",
            subtitle = "Review regional dispatch notifications and SLA state",
            icon = Icons.Default.Notifications,
            onClick = onNavigateToNotifications
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickActionCard(
            title = "Regional Fleet Readiness",
            subtitle = "Monitor station appliance status across the region",
            icon = Icons.Default.DirectionsBus,
            onClick = onNavigateToAppliances
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickActionCard(
            title = "Regional Administrator Profile",
            subtitle = "View regional scope and account details",
            icon = Icons.Default.Person,
            onClick = onNavigateToProfile
        )
    }
}
