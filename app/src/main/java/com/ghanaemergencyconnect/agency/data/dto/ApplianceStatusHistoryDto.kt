package com.ghanaemergencyconnect.agency.data.dto

data class ApplianceStatusHistoryDto(
    val id: String,
    val applianceId: String,
    val previousStatus: String?,
    val newStatus: String,
    val reason: String?,
    val incidentId: String?,
    val changedBy: String?,
    val changedAt: String
)
