package com.ghanaemergencyconnect.agency.data.domain.model

data class StaffNotificationItem(
    val id: String,
    val reportId: String?,
    val assignmentId: String?,
    val eventType: String,
    val title: String,
    val body: String,
    val readAt: String?,
    val createdAt: String
) {
    val isRead: Boolean
        get() = !readAt.isNullOrBlank()
}
