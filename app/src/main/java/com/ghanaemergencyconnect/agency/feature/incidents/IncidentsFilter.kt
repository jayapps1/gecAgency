package com.ghanaemergencyconnect.agency.feature.incidents

import com.ghanaemergencyconnect.agency.data.domain.model.IncidentAssignmentStatus

enum class IncidentsFilter(val label: String) {
    ALL_ACTIVE("All Active"),
    RECEIVED("Received"),
    ACKNOWLEDGED("Acknowledged"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved");

    fun matches(status: IncidentAssignmentStatus): Boolean {
        return when (this) {
            ALL_ACTIVE -> status == IncidentAssignmentStatus.RECEIVED ||
                    status == IncidentAssignmentStatus.ACKNOWLEDGED ||
                    status == IncidentAssignmentStatus.ASSIGNED ||
                    status == IncidentAssignmentStatus.IN_PROGRESS
            RECEIVED -> status == IncidentAssignmentStatus.RECEIVED || status == IncidentAssignmentStatus.ASSIGNED || status == IncidentAssignmentStatus.SUBMITTED
            ACKNOWLEDGED -> status == IncidentAssignmentStatus.ACKNOWLEDGED
            IN_PROGRESS -> status == IncidentAssignmentStatus.IN_PROGRESS
            RESOLVED -> status == IncidentAssignmentStatus.RESOLVED || status == IncidentAssignmentStatus.CLOSED
        }
    }
}
