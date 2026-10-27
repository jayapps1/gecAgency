package com.ghanaemergencyconnect.agency.data.dto

data class AssignResponderRequest(
    val responderUserId: String,
    val note: String? = null
)
