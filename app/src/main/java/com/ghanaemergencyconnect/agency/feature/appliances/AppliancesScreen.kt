package com.ghanaemergencyconnect.agency.feature.appliances

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
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ghanaemergencyconnect.agency.R
import com.ghanaemergencyconnect.agency.core.ui.component.GecGlassCard
import com.ghanaemergencyconnect.agency.core.ui.component.GecTopAppBar
import com.ghanaemergencyconnect.agency.data.domain.model.ApplianceStatus
import com.ghanaemergencyconnect.agency.data.domain.model.OperationalAppliance
import com.ghanaemergencyconnect.agency.data.domain.model.StaffRole

/**
 * Presents the appliance's current operational state while preserving the
 * distinction between permanent home station and present current station.
 */
@Composable
fun AppliancesScreen(
    role: StaffRole?,
    uiState: AppliancesUiState,
    onFilterSelected: (ApplianceStatus?) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onRefresh: () -> Unit,
    onBackClick: (() -> Unit)? = null
) {
    LaunchedEffect(Unit) {
        onRefresh()
    }

    val screenTitle = when (role) {
        StaffRole.AGENCY_ADMIN -> "Agency Fleet"
        StaffRole.REGIONAL_ADMIN -> "Regional Fleet"
        StaffRole.STATION_ADMIN -> "Station Appliances"
        StaffRole.CONTROL_ROOM_OPERATOR -> "Appliance Status"
        StaffRole.RESPONDER -> "Appliance Availability"
        else -> "Fleet Availability"
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
                // 2-Column Readiness Metric Grid
                if (uiState.appliances.isNotEmpty()) {
                    val availableCount = uiState.appliances.count { it.status == ApplianceStatus.AVAILABLE }
                    val dispatchedCount = uiState.appliances.count { it.status == ApplianceStatus.DISPATCHED }
                    val maintenanceCount = uiState.appliances.count { it.status == ApplianceStatus.MAINTENANCE }
                    val outOfCommissionCount = uiState.appliances.count { it.status == ApplianceStatus.OUT_OF_COMMISSION }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        FleetMetricCard(
                            label = "Available",
                            count = availableCount,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )
                        FleetMetricCard(
                            label = "Dispatched",
                            count = dispatchedCount,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        FleetMetricCard(
                            label = "Maintenance",
                            count = maintenanceCount,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.weight(1f)
                        )
                        FleetMetricCard(
                            label = "Out of Commission",
                            count = outOfCommissionCount,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Search Input
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = onSearchQueryChanged,
                    placeholder = { Text("Search by Call Sign or Station...") },
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
                    item {
                        FilterChip(
                            selected = uiState.activeFilter == null,
                            onClick = { onFilterSelected(null) },
                            label = { Text("All") }
                        )
                    }
                    items(listOf(ApplianceStatus.AVAILABLE, ApplianceStatus.DISPATCHED, ApplianceStatus.OUT_OF_COMMISSION, ApplianceStatus.MAINTENANCE)) { status ->
                        FilterChip(
                            selected = uiState.activeFilter == status,
                            onClick = { onFilterSelected(status) },
                            label = { Text(status.label) }
                        )
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
                                text = "Loading fleet availability...",
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
                            Button(onClick = onRefresh) { Text("Retry") }
                        }
                    }

                    uiState.filteredAppliances.isEmpty() -> {
                        GecGlassCard(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "No appliances match current filters or search.",
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
                            items(uiState.filteredAppliances.size) { index ->
                                val item = uiState.filteredAppliances[index]
                                GecApplianceCard(appliance = item)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FleetMetricCard(
    label: String,
    count: Int,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    GecGlassCard(modifier = modifier) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.headlineMedium,
            color = color
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun GecApplianceCard(
    appliance: OperationalAppliance
) {
    val statusColor = when (appliance.status) {
        ApplianceStatus.AVAILABLE -> MaterialTheme.colorScheme.primary
        ApplianceStatus.DISPATCHED -> MaterialTheme.colorScheme.secondary
        ApplianceStatus.OUT_OF_COMMISSION -> MaterialTheme.colorScheme.error
        ApplianceStatus.MAINTENANCE -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    GecGlassCard(
        borderColor = if (appliance.status == ApplianceStatus.OUT_OF_COMMISSION) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor.copy(alpha = 0.15f))
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsBus,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = appliance.callSign,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = appliance.displayName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Status Chip
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(statusColor.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = appliance.status.label.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = statusColor
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (appliance.isRelocated) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Temporarily Relocated • Current: ${appliance.effectiveStationName} (Home: ${appliance.homeStationName ?: "Main Station"})",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        } else {
            Text(
                text = "Station: ${appliance.effectiveStationName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
