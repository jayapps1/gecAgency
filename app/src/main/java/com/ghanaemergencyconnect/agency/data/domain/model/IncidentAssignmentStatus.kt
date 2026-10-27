package com.ghanaemergencyconnect.agency.data.domain.model

enum class IncidentAssignmentStatus(val label: String) {
    SUBMITTED("Submitted"),
    RECEIVED("Received"),
    ACKNOWLEDGED("Acknowledged"),
    ASSIGNED("Assigned"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved"),
    CLOSED("Closed"),
    REJECTED("Rejected"),
    TRANSFERRED("Transferred"),
    UNKNOWN("Unknown");

    val isTerminal: Boolean
        get() = this == RESOLVED || this == CLOSED || this == REJECTED

    companion object {
        fun fromCode(code: String?): IncidentAssignmentStatus {
            if (code.isNullOrBlank()) return UNKNOWN
            return try {
                valueOf(code.uppercase())
            } catch (e: Exception) {
                UNKNOWN
            }
        }
    }
}
