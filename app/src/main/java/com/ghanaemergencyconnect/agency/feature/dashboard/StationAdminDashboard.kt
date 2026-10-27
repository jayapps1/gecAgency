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
fun StationAdminDashboard(
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
            text = "Station Quick Actions",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickActionCard(
            title = "Station Incidents",
            subtitle = "Manage active incidents assigned to station",
            icon = Icons.Default.ReportProblem,
            onClick = onNavigateToIncidents
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickActionCard(
            title = "Station Notifications",
            subtitle = "Review station dispatch alerts and history",
            icon = Icons.Default.Notifications,
            onClick = onNavigateToNotifications
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickActionCard(
            title = "Station Fleet Readiness",
            subtitle = "Manage station appliances and availability",
            icon = Icons.Default.DirectionsBus,
            onClick = onNavigateToAppliances
        )

        Spacer(modifier = Modifier.height(12.dp))

        QuickActionCard(
            title = "Station Administrator Profile",
            subtitle = "View station details and account information",
            icon = Icons.Default.Person,
            onClick = onNavigateToProfile
        )
    }
}
