package com.ghanaemergencyconnect.agency.data.mapper

import com.ghanaemergencyconnect.agency.data.domain.model.StaffNotificationItem
import com.ghanaemergencyconnect.agency.data.dto.StaffNotificationDto

fun StaffNotificationDto.toDomain(): StaffNotificationItem {
    return StaffNotificationItem(
        id = id,
        reportId = reportId,
        assignmentId = assignmentId,
        eventType = eventType,
        title = title,
        body = body,
        readAt = readAt,
        createdAt = createdAt
    )
}
