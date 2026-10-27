package com.ghanaemergencyconnect.agency.data.domain.model

data class OperationalIncident(
    val reportId: String,
    val publicReference: String,
    val categoryId: String?,
    val categoryName: String,
    val mode: String?,
    val severity: String?,
    val status: com.ghanaemergencyconnect.agency.data.domain.model.IncidentAssignmentStatus,
    val createdAt: String,
    val targetAgencyTypes: Set<String>,
    val acknowledgementSlaState: String,
    val responseStartSlaState: String
)
