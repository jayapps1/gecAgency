package com.ghanaemergencyconnect.agency.feature.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ghanaemergencyconnect.agency.R
import com.ghanaemergencyconnect.agency.core.ui.component.GecGlassCard
import com.ghanaemergencyconnect.agency.core.ui.component.GecTopAppBar
import com.ghanaemergencyconnect.agency.data.domain.model.AuthenticatedStaff
import com.ghanaemergencyconnect.agency.data.domain.model.StaffRole

/**
 * Presents role-scoped operational information for the authenticated staff member.
 * Metrics and actions are derived from real backend state; the dashboard never
 * manufactures placeholder statistics for visual completeness.
 */
@Composable
fun DashboardScreen(
    staff: AuthenticatedStaff?,
    onNavigateToIncidents: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToAppliances: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Scaffold(
        topBar = {
            GecTopAppBar(title = stringResource(R.string.nav_dashboard))
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                if (staff != null) {
                    when (staff.primaryRole) {
                        StaffRole.AGENCY_ADMIN -> AgencyAdminDashboard(
                            staff = staff,
                            onNavigateToIncidents = onNavigateToIncidents,
                            onNavigateToNotifications = onNavigateToNotifications,
                            onNavigateToAppliances = onNavigateToAppliances,
                            onNavigateToProfile = onNavigateToProfile
                        )

                        StaffRole.REGIONAL_ADMIN -> RegionalAdminDashboard(
                            staff = staff,
                            onNavigateToIncidents = onNavigateToIncidents,
                            onNavigateToNotifications = onNavigateToNotifications,
                            onNavigateToAppliances = onNavigateToAppliances,
                            onNavigateToProfile = onNavigateToProfile
                        )

                        StaffRole.STATION_ADMIN -> StationAdminDashboard(
                            staff = staff,
                            onNavigateToIncidents = onNavigateToIncidents,
                            onNavigateToNotifications = onNavigateToNotifications,
                            onNavigateToAppliances = onNavigateToAppliances,
                            onNavigateToProfile = onNavigateToProfile
                        )

                        StaffRole.CONTROL_ROOM_OPERATOR -> ControlRoomDashboard(
                            staff = staff,
                            onNavigateToIncidents = onNavigateToIncidents,
                            onNavigateToNotifications = onNavigateToNotifications,
                            onNavigateToAppliances = onNavigateToAppliances,
                            onNavigateToProfile = onNavigateToProfile
                        )

                        StaffRole.RESPONDER -> ResponderDashboard(
                            staff = staff,
                            onNavigateToIncidents = onNavigateToIncidents,
                            onNavigateToNotifications = onNavigateToNotifications,
                            onNavigateToAppliances = onNavigateToAppliances,
                            onNavigateToProfile = onNavigateToProfile
                        )

                        StaffRole.SYSTEM_ADMIN -> SystemAdminDashboard(
                            staff = staff,
                            onNavigateToNotifications = onNavigateToNotifications,
                            onNavigateToProfile = onNavigateToProfile
                        )

                        StaffRole.REPORT_REVIEWER,
                        StaffRole.AUDITOR,
                        StaffRole.UNKNOWN -> RestrictedDashboard(
                            staff = staff,
                            onNavigateToProfile = onNavigateToProfile
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Loading staff profile...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GecGlassCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
