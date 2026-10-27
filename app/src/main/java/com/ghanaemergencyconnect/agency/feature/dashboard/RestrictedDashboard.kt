package com.ghanaemergencyconnect.agency.feature.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ghanaemergencyconnect.agency.core.ui.component.GecGlassCard
import com.ghanaemergencyconnect.agency.core.ui.component.OperationalScopeCard
import com.ghanaemergencyconnect.agency.data.domain.model.AuthenticatedStaff

@Composable
fun RestrictedDashboard(
    staff: AuthenticatedStaff,
    onNavigateToProfile: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OperationalScopeCard(staff = staff)

        Spacer(modifier = Modifier.height(16.dp))

        GecGlassCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Restricted Access",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Mobile operational features are restricted for your role.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        QuickActionCard(
            title = "Staff Profile",
            subtitle = "View account details and assigned roles",
            icon = Icons.Default.Person,
            onClick = onNavigateToProfile
        )
    }
}
