package com.ghanaemergencyconnect.agency.data.dto

data class CitizenReviewDto(
    val id: String?,
    val reportId: String?,
    val assignmentId: String?,
    val agencyName: String?,
    val stationName: String?,
    val rating: Int?,
    val feedback: String?,
    val createdAt: String?
)
