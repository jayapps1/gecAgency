package com.ghanaemergencyconnect.agency.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ghanaemergencyconnect.agency.data.domain.model.AuthenticatedStaff

/**
 * Role and operational scope come from the authenticated backend profile.
 * Rendered using a glassmorphic operational card.
 */
@Composable
fun OperationalScopeCard(
    staff: AuthenticatedStaff,
    modifier: Modifier = Modifier
) {
    GecGlassCard(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Initials Avatar
            val initials = "${staff.firstName.take(1)}${staff.lastName.take(1)}".uppercase().ifBlank { "GS" }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = initials,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = staff.displayName,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(2.dp))

                // Role Chip Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = staff.primaryRoleLabel.uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                val scopeText = buildString {
                    if (!staff.agencyName.isNullOrBlank()) append(staff.agencyName)
                    if (!staff.region.isNullOrBlank()) {
                        if (isNotEmpty()) append(" • ")
                        append(staff.region)
                    }
                    if (!staff.stationName.isNullOrBlank()) {
                        if (isNotEmpty()) append(" • ")
                        append(staff.stationName)
                    }
                }
                if (scopeText.isNotBlank()) {
                    Text(
                        text = scopeText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
