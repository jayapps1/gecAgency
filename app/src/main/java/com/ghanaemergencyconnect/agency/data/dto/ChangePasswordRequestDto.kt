package com.ghanaemergencyconnect.agency.data.dto

data class ChangePasswordRequestDto(
    val currentPassword: String,
    val newPassword: String
)
