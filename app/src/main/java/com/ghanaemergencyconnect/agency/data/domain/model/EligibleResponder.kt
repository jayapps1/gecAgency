package com.ghanaemergencyconnect.agency.data.domain.model

data class EligibleResponder(
    val id: String,
    val fullName: String,
    val email: String,
    val role: String,
    val stationName: String?,
    val active: Boolean,
    val assigned: Boolean
)
