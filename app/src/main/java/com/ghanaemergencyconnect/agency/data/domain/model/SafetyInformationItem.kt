package com.ghanaemergencyconnect.agency.data.domain.model

data class SafetyInformationItem(
    val id: String,
    val agencyId: String?,
    val title: String,
    val content: String,
    val category: String,
    val severity: String,
    val status: String,
    val notifyUsers: Boolean,
    val publishedAt: String?,
    val createdAt: String
)
