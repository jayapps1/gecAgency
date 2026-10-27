package com.ghanaemergencyconnect.agency.data.dto

data class ReportStatusUpdateRequest(
    val status: String,
    val note: String? = null
)
