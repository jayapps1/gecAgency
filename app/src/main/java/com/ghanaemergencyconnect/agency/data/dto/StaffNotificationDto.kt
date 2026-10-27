package com.ghanaemergencyconnect.agency.data.dto

data class StaffNotificationDto(
    val id: String,
    val staffUserId: String,
    val reportId: String?,
    val assignmentId: String?,
    val eventType: String,
    val title: String,
    val body: String,
    val readAt: String?,
    val createdAt: String
)
