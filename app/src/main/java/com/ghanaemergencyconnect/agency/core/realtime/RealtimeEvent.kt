package com.ghanaemergencyconnect.agency.core.realtime

data class RealtimeEvent(
    val name: String,
    val reportId: String? = null,
    val assignmentId: String? = null,
    val applianceId: String? = null,
    val notificationId: String? = null,
    val message: String? = null
)
