package com.ghanaemergencyconnect.agency.data.dto

data class ResponseApplianceDto(
    val id: String,
    val agencyId: String,
    val homeStationId: String,
    val homeStationName: String?,
    val currentStationId: String?,
    val currentStationName: String?,
    val resourceType: String?,
    val callSign: String,
    val registrationNumber: String?,
    val displayName: String?,
    val operationalStatus: String?,
    val statusReason: String?,
    val active: Boolean,
    val updatedAt: String?
)
