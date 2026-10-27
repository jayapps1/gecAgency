package com.ghanaemergencyconnect.agency.data.dto

data class EligibleResponderDto(
    val id: String,
    val fullName: String,
    val email: String,
    val role: String,
    val agencyId: String?,
    val agencyName: String?,
    val stationId: String?,
    val stationName: String?,
    val active: Boolean,
    val assigned: Boolean
)
