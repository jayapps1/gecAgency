package com.ghanaemergencyconnect.agency.data.dto

data class SafetyInformationDto(
    val id: String,
    val sourceType: String?,
    val agencyId: String?,
    val title: String,
    val summary: String?,
    val content: String,
    val category: String?,
    val severity: String?,
    val status: String?,
    val notifyUsers: Boolean,
    val publishedAt: String?,
    val expiresAt: String?,
    val createdAt: String?
)
