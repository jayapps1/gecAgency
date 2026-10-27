package com.ghanaemergencyconnect.agency.data.dto

data class LoginResponseDto(
    val accessToken: String?,
    val expiresInSeconds: Long,
    val user: AdminUserResponseDto?,
    val roles: Set<String>?,
    val permissions: Set<String>?,
    val authenticationState: String?,
    val mfaChallenge: String?
)
