package com.ghanaemergencyconnect.agency.data.dto

data class ReportTransferRequest(
    val targetAgencyType: String,
    val note: String? = null
)
