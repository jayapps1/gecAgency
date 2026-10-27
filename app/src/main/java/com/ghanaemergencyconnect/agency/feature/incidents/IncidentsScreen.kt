package com.ghanaemergencyconnect.agency.feature.incidents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ghanaemergencyconnect.agency.core.ui.component.GecGlassCard
import com.ghanaemergencyconnect.agency.core.ui.component.GecTopAppBar
import com.ghanaemergencyconnect.agency.core.util.SlaTimerUtils
import com.ghanaemergencyconnect.agency.data.domain.model.IncidentAssignmentStatus
import com.ghanaemergencyconnect.agency.data.domain.model.OperationalIncident
import com.ghanaemergencyconnect.agency.data.domain.model.StaffRole

/**
 * Presents only the operational fields required to triage an incident from
 * the queue. Full report details remain on the detail screen to keep the
 * live queue fast to scan.
 */
@Composable
fun IncidentsScreen(
    role: StaffRole?,
    uiState: IncidentsUiState,
    onFilterSelected: (IncidentsFilter) -> Unit,
    onRefresh: () -> Unit,
    onIncidentClick: (String) -> Unit,
    onBackClick: (() -> Unit)? = null
) {
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        onRefresh()
    }

    val screenTitle = when (role) {
        StaffRole.AGENCY_ADMIN -> "Agency Incidents"
        StaffRole.REGIONAL_ADMIN -> "Regional Incidents"
        StaffRole.STATION_ADMIN -> "Station Incidents"
        StaffRole.CONTROL_ROOM_OPERATOR -> "Control Room Queue"
        StaffRole.RESPONDER -> "Active Incidents"
        else -> "Operational Incidents"
    }

    Scaffold(
        topBar = {
            GecTopAppBar(
                title = screenTitle,
                onBackClick = onBackClick
            )
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
                    .padding(16.dp)
            ) {
                // Search Input
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by reference, category, or location...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                // Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    items(IncidentsFilter.entries) { filter ->
                        FilterChip(
                            selected = uiState.activeFilter == filter,
                            onClick = { onFilterSelected(filter) },
                            label = { Text(filter.label) }
                        )
                    }
                }

                val displayedIncidents = if (searchQuery.isBlank()) {
                    uiState.filteredIncidents
                } else {
                    uiState.filteredIncidents.filter { item ->
                        item.publicReference.contains(searchQuery, ignoreCase = true) ||
                                item.categoryName.contains(searchQuery, ignoreCase = true)
                    }
                }

                when {
                    uiState.isLoading -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Loading operational queue...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    !uiState.errorMessage.isNullOrBlank() -> {
                        GecGlassCard(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = uiState.errorMessage,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = onRefresh) {
                                Text("Retry")
                            }
                        }
                    }

                    displayedIncidents.isEmpty() -> {
                        GecGlassCard(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "No active incidents match the active filter or search.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(displayedIncidents.size) { index ->
                                val incident = displayedIncidents[index]
                                IncidentCard(
                                    incident = incident,
                                    onClick = { onIncidentClick(incident.reportId) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IncidentCard(
    incident: OperationalIncident,
    onClick: () -> Unit
) {
    val isDelayed = incident.responseStartSlaState.uppercase() == "DELAYED"

    GecGlassCard(
        onClick = onClick,
        borderColor = if (isDelayed) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = if (isDelayed) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = incident.publicReference,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Status Chip
            val statusColor = when (incident.status) {
                IncidentAssignmentStatus.RECEIVED, IncidentAssignmentStatus.ASSIGNED -> MaterialTheme.colorScheme.secondary
                IncidentAssignmentStatus.ACKNOWLEDGED -> MaterialTheme.colorScheme.primary
                IncidentAssignmentStatus.IN_PROGRESS -> MaterialTheme.colorScheme.tertiary
                IncidentAssignmentStatus.RESOLVED, IncidentAssignmentStatus.CLOSED -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(statusColor.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = incident.status.label.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = statusColor
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = incident.categoryName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val slaLabel = SlaTimerUtils.getSlaBadgeLabel(incident.responseStartSlaState)
            val slaColor = when (incident.responseStartSlaState.uppercase()) {
                "DELAYED" -> MaterialTheme.colorScheme.error
                "ATTENTION" -> MaterialTheme.colorScheme.secondary
                else -> MaterialTheme.colorScheme.primary
            }

            Text(
                text = "SLA: $slaLabel",
                style = MaterialTheme.typography.labelMedium,
                color = slaColor
            )

            Text(
                text = "Received: ${incident.createdAt.take(10)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
