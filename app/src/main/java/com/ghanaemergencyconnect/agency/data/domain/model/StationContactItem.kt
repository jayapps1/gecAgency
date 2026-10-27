package com.ghanaemergencyconnect.agency.data.domain.model

data class StationContactItem(
    val id: String,
    val stationId: String,
    val contactNumber: String,
    val contactLabel: String?,
    val priority: Int,
    val verified: Boolean
)
