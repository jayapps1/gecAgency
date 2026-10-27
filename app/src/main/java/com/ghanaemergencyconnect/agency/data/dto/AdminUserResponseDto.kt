package com.ghanaemergencyconnect.agency.data.dto

data class AdminUserResponseDto(
    val id: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val phoneNumber: String?,
    val agencyId: String?,
    val agencyName: String?,
    val region: String?,
    val stationId: String?,
    val stationName: String?,
    val active: Boolean,
    val locked: Boolean,
    val mustChangePassword: Boolean,
    val roles: Set<String>?
)
