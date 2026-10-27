package com.ghanaemergencyconnect.agency.data.domain.model

data class ResponderAssignment(
    val id: String,
    val reportAssignmentId: String,
    val reportId: String,
    val publicReference: String,
    val categoryName: String,
    val responderUserId: String,
    val responderName: String,
    val responderRole: String,
    val stationName: String?,
    val assignedAt: String,
    val releasedAt: String?,
    val active: Boolean
)
