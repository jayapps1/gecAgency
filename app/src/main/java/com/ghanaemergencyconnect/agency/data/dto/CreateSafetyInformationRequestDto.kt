package com.ghanaemergencyconnect.agency.data.dto

data class CreateSafetyInformationRequestDto(
    val title: String,
    val content: String,
    val category: String = "GENERAL",
    val severity: String = "INFO",
    val notifyUsers: Boolean = false,
    val expiresAt: String? = null
)
