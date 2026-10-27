package com.ghanaemergencyconnect.agency.data.dto

data class ResponderAssignmentDto(
    val id: String,
    val reportAssignmentId: String,
    val reportId: String,
    val publicReference: String,
    val categoryName: String,
    val responderUserId: String,
    val responderName: String?,
    val responderEmail: String?,
    val responderRole: String?,
    val stationId: String?,
    val stationName: String?,
    val assignedAt: String,
    val releasedAt: String?,
    val active: Boolean
)
