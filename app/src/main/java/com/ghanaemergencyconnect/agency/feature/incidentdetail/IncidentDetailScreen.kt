package com.ghanaemergencyconnect.agency.feature.incidentdetail

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ghanaemergencyconnect.agency.core.ui.component.GecGlassCard
import com.ghanaemergencyconnect.agency.core.ui.component.GecTopAppBar
import com.ghanaemergencyconnect.agency.core.util.SlaTimerUtils
import com.ghanaemergencyconnect.agency.data.domain.model.AuthenticatedStaff
import com.ghanaemergencyconnect.agency.data.domain.model.EligibleResponder
import com.ghanaemergencyconnect.agency.data.domain.model.IncidentAssignmentStatus
import com.ghanaemergencyconnect.agency.data.domain.model.ResponderAssignment
import com.ghanaemergencyconnect.agency.data.domain.model.StaffRole

/**
 * Responder assignments target an agency-specific ReportAssignment rather
 * than the public report itself, preserving independent operations when
 * multiple emergency services respond to the same incident.
 */
@Composable
fun IncidentDetailScreen(
    reportId: String,
    staff: AuthenticatedStaff?,
    uiState: IncidentDetailUiState,
    onRefresh: () -> Unit,
    onAcknowledge: () -> Unit,
    onStartAction: () -> Unit,
    onResolve: (String?) -> Unit,
    onLoadEligibleResponders: () -> Unit = {},
    onAssignResponder: (String) -> Unit = {},
    onReleaseResponder: (String) -> Unit = {},
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var showResolveDialog by remember { mutableStateOf(false) }
    var showAssignDialog by remember { mutableStateOf(false) }
    var releaseTarget by remember { mutableStateOf<ResponderAssignment?>(null) }

    LaunchedEffect(reportId) {
        onRefresh()
    }

    val detail = uiState.detail
    val isResponder = staff?.primaryRole == StaffRole.RESPONDER || staff?.primaryRole == StaffRole.SYSTEM_ADMIN
    val myAssignment = detail?.findMyAgencyAssignment(staff?.agencyId)
    val currentStatus = myAssignment?.status ?: detail?.status
    val responseSla = myAssignment?.responseStartSlaState ?: "ON_TIME"

    if (showResolveDialog) {
        AlertDialog(
            onDismissRequest = { showResolveDialog = false },
            title = { Text("Resolve Incident Assignment") },
            text = { Text("Confirm that operational response is complete and resolve this assignment?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResolveDialog = false
                        onResolve(null)
                    }
                ) {
                    Text("Resolve")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResolveDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showAssignDialog) {
        AlertDialog(
            onDismissRequest = { showAssignDialog = false },
            title = { Text("Assign Responder") },
            text = {
                Column {
                    Text("Select eligible responder for dispatch:")
                    Spacer(modifier = Modifier.height(12.dp))
                    if (uiState.isLoadingResponders) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else if (uiState.eligibleResponders.isEmpty()) {
                        Text("No active responders available in agency scope.", style = MaterialTheme.typography.bodySmall)
                    } else {
                        uiState.eligibleResponders.forEach { responder ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = responder.fullName, style = MaterialTheme.typography.bodyMedium)
                                    Text(
                                        text = "${responder.role}${responder.stationName?.let { " • $it" } ?: ""}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (responder.assigned) {
                                    Text("Assigned", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                                } else {
                                    Button(
                                        onClick = {
                                            showAssignDialog = false
                                            onAssignResponder(responder.id)
                                        },
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Assign")
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAssignDialog = false }) { Text("Close") }
            }
        )
    }

    if (releaseTarget != null) {
        AlertDialog(
            onDismissRequest = { releaseTarget = null },
            title = { Text("Release Responder") },
            text = { Text("Release ${releaseTarget?.responderName} from active incident assignment?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val targetId = releaseTarget?.id
                        releaseTarget = null
                        if (targetId != null) {
                            onReleaseResponder(targetId)
                        }
                    }
                ) {
                    Text("Release", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { releaseTarget = null }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            GecTopAppBar(
                title = detail?.publicReference ?: "Incident Detail",
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
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                when {
                    uiState.isLoading -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Loading incident details...",
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

                    detail != null -> {
                        // Hero Glass Card
                        val isDelayed = responseSla.uppercase() == "DELAYED"
                        GecGlassCard(
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
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = detail.publicReference,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                // Status Chip
                                val statusColor = when (currentStatus) {
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
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = (currentStatus ?: IncidentAssignmentStatus.UNKNOWN).label.uppercase(),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = statusColor
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Category: ${detail.categoryName}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val slaLabel = SlaTimerUtils.getSlaBadgeLabel(responseSla)
                                val slaColor = when (responseSla.uppercase()) {
                                    "DELAYED" -> MaterialTheme.colorScheme.error
                                    "ATTENTION" -> MaterialTheme.colorScheme.secondary
                                    else -> MaterialTheme.colorScheme.primary
                                }

                                Text(
                                    text = "SLA: $slaLabel",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = slaColor
                                )

                                Text(
                                    text = "Reported: ${detail.createdAt.take(10)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Location & Navigation Card
                        GecGlassCard(modifier = Modifier.fillMaxWidth()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Location & Overview",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = detail.locationDisplayLabel,
                                style = MaterialTheme.typography.bodyLarge
                            )

                            if (!detail.description.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = detail.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (detail.latitude != null && detail.longitude != null) {
                                Spacer(modifier = Modifier.height(12.dp))
                                OutlinedButton(
                                    onClick = {
                                        val mapIntent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("geo:${detail.latitude},${detail.longitude}?q=${detail.latitude},${detail.longitude}(Incident+Location)")
                                        )
                                        try {
                                            context.startActivity(mapIntent)
                                        } catch (e: Exception) {
                                            // Map app unavailable
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Map,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Open Location in Maps")
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Assigned Responders Card
                        GecGlassCard(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Badge,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Assigned Responders",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                if (!isResponder) {
                                    OutlinedButton(
                                        onClick = {
                                            onLoadEligibleResponders()
                                            showAssignDialog = true
                                        },
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Assign")
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            val activeResponders = uiState.assignedResponders.filter { it.active }
                            if (activeResponders.isEmpty()) {
                                Text(
                                    text = "No responders currently assigned to this incident.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                activeResponders.forEachIndexed { index, resp ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .clip(CircleShape)
                                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Person,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text(text = resp.responderName, style = MaterialTheme.typography.bodyMedium)
                                                Text(
                                                    text = "${resp.responderRole}${resp.stationName?.let { " • $it" } ?: ""}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }

                                        if (!isResponder) {
                                            IconButton(onClick = { releaseTarget = resp }) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Release responder",
                                                    tint = MaterialTheme.colorScheme.error,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }
                                    if (index < activeResponders.size - 1) {
                                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 4.dp))
                                    }
                                }
                            }
                        }

                        // Operational Actions Area
                        if (!isResponder && currentStatus != null && !currentStatus.isTerminal) {
                            Spacer(modifier = Modifier.height(16.dp))

                            GecGlassCard(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Operational Action Panel",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                when (currentStatus) {
                                    IncidentAssignmentStatus.SUBMITTED,
                                    IncidentAssignmentStatus.ASSIGNED,
                                    IncidentAssignmentStatus.RECEIVED -> {
                                        Button(
                                            onClick = onAcknowledge,
                                            enabled = !uiState.isPerformingAction,
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(50.dp)
                                        ) {
                                            if (uiState.isPerformingAction) {
                                                CircularProgressIndicator(
                                                    color = MaterialTheme.colorScheme.onPrimary,
                                                    strokeWidth = 2.dp,
                                                    modifier = Modifier.size(20.dp).padding(end = 8.dp)
                                                )
                                            }
                                            Text("Acknowledge Incident Assignment")
                                        }
                                    }

                                    IncidentAssignmentStatus.ACKNOWLEDGED -> {
                                        Button(
                                            onClick = onStartAction,
                                            enabled = !uiState.isPerformingAction,
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(50.dp)
                                        ) {
                                            if (uiState.isPerformingAction) {
                                                CircularProgressIndicator(
                                                    color = MaterialTheme.colorScheme.onPrimary,
                                                    strokeWidth = 2.dp,
                                                    modifier = Modifier.size(20.dp).padding(end = 8.dp)
                                                )
                                            }
                                            Text("Start Operational Response")
                                        }
                                    }

                                    IncidentAssignmentStatus.IN_PROGRESS -> {
                                        Button(
                                            onClick = { showResolveDialog = true },
                                            enabled = !uiState.isPerformingAction,
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary
                                            ),
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(50.dp)
                                        ) {
                                            if (uiState.isPerformingAction) {
                                                CircularProgressIndicator(
                                                    color = MaterialTheme.colorScheme.onPrimary,
                                                    strokeWidth = 2.dp,
                                                    modifier = Modifier.size(20.dp).padding(end = 8.dp)
                                                )
                                            }
                                            Text("Resolve Assignment")
                                        }
                                    }

                                    else -> { /* Terminal state */ }
                                }
                            }
                        }

                        // Timeline History Card
                        if (detail.statusHistory.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))

                            GecGlassCard(modifier = Modifier.fillMaxWidth()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.History,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Timeline History",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                detail.statusHistory.forEachIndexed { index, item ->
                                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = item.status.label,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = item.timestamp.take(16),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        if (!item.note.isNullOrBlank()) {
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = item.note,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        if (index < detail.statusHistory.size - 1) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
