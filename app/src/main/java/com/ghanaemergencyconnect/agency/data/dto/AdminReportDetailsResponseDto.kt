package com.ghanaemergencyconnect.agency.data.dto

data class AdminReportDetailsResponseDto(
    val id: String,
    val publicReference: String,
    val categoryId: String?,
    val categoryName: String?,
    val mode: String?,
    val severity: String?,
    val status: String?,
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
    val createdAt: String?,
    val updatedAt: String?,
    val targetAgencyTypes: Set<String>?,
    val statusHistory: List<StatusHistoryDto>?,
    val attachments: List<AttachmentDto>?,
    val agencyAssignments: List<AgencyAssignmentDto>?
) {
    data class StatusHistoryDto(
        val status: String?,
        val note: String?,
        val timestamp: String?
    )

    data class AttachmentDto(
        val id: String,
        val reportId: String,
        val mediaType: String?,
        val contentType: String?,
        val originalFileName: String?,
        val fileSizeBytes: Long?,
        val createdAt: String?
    )

    data class AgencyAssignmentDto(
        val assignmentId: String,
        val agencyType: String?,
        val agencyId: String?,
        val agencyName: String?,
        val stationId: String?,
        val stationName: String?,
        val status: String?,
        val assignedAt: String?,
        val updatedAt: String?,
        val acknowledgementSlaState: String?,
        val responseStartSlaState: String?
    )
}
