package com.ghanaemergencyconnect.agency.data.dto

data class AdminReportSummaryResponseDto(
    val id: String,
    val publicReference: String,
    val categoryId: String?,
    val categoryName: String?,
    val mode: String?,
    val severity: String?,
    val status: String?,
    val createdAt: String?,
    val targetAgencyTypes: Set<String>?,
    val acknowledgementSlaState: String?,
    val responseStartSlaState: String?
)
