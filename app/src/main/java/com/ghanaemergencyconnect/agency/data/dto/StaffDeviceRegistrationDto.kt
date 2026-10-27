package com.ghanaemergencyconnect.agency.data.dto

data class StaffDeviceRegistrationDto(
    val installationId: String,
    val pushToken: String,
    val platform: String = "ANDROID",
    val deviceLabel: String? = null,
    val appVersion: String? = null
)
