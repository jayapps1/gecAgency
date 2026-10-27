package com.ghanaemergencyconnect.agency.data.dto

data class StationContactDto(
    val id: String,
    val stationId: String,
    val contactNumber: String,
    val contactLabel: String?,
    val priority: Int,
    val verified: Boolean
)
