package com.ghanaemergencyconnect.agency.data.domain.model

data class CitizenReviewItem(
    val id: String,
    val reportId: String?,
    val assignmentId: String?,
    val agencyName: String,
    val stationName: String?,
    val rating: Int,
    val feedback: String?,
    val createdAt: String
)
