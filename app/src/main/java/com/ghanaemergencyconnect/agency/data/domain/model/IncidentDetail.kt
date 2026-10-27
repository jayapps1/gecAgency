package com.ghanaemergencyconnect.agency.data.domain.model

data class IncidentDetail(
    val reportId: String,
    val publicReference: String,
    val categoryId: String?,
    val categoryName: String,
    val mode: String?,
    val severity: String?,
    val status: com.ghanaemergencyconnect.agency.data.domain.model.IncidentAssignmentStatus,
    val description: String?,
    val latitude: Double?,
    val longitude: Double?,
    val locationAccuracyMeters: Double?,
    val landmark: String?,
    val locality: String?,
    val district: String?,
    val region: String?,
    val vehicleRegistrationNumber: String?,
    val vehicleType: String?,
    val vehicleColour: String?,
    val directionOfTravel: String?,
    val createdAt: String,
    val updatedAt: String,
    val targetAgencyTypes: Set<String>,
    val statusHistory: List<StatusHistoryItem>,
    val attachments: List<AttachmentItem>,
    val agencyAssignments: List<AgencyAssignmentItem>
) {
    data class StatusHistoryItem(
        val status: com.ghanaemergencyconnect.agency.data.domain.model.IncidentAssignmentStatus,
        val note: String?,
        val timestamp: String
    )

    data class AttachmentItem(
        val id: String,
        val reportId: String,
        val mediaType: String?,
        val contentType: String?,
        val originalFileName: String?,
        val fileSizeBytes: Long?,
        val createdAt: String
    )

    data class AgencyAssignmentItem(
        val assignmentId: String,
        val agencyType: String,
        val agencyId: String?,
        val agencyName: String?,
        val stationId: String?,
        val stationName: String?,
        val status: com.ghanaemergencyconnect.agency.data.domain.model.IncidentAssignmentStatus,
        val assignedAt: String,
        val updatedAt: String,
        val acknowledgementSlaState: String,
        val responseStartSlaState: String
    )

    fun findMyAgencyAssignment(staffAgencyId: String?): AgencyAssignmentItem? {
        if (agencyAssignments.isEmpty()) return null
        if (staffAgencyId != null) {
            val match = agencyAssignments.firstOrNull { it.agencyId == staffAgencyId }
            if (match != null) return match
        }
        return agencyAssignments.firstOrNull()
    }

    val locationDisplayLabel: String
        get() = buildString {
            val parts = listOfNotNull(landmark, locality, district, region).filter { it.isNotBlank() }
            if (parts.isNotEmpty()) {
                append(parts.joinToString(", "))
            } else if (latitude != null && longitude != null) {
                append("Lat: $latitude, Lng: $longitude")
            } else {
                append("Location unavailable")
            }
        }
}
