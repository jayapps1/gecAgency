package com.ghanaemergencyconnect.agency.data.dto

data class TotpLoginRequestDto(
    val email: String,
    val code: String
)
