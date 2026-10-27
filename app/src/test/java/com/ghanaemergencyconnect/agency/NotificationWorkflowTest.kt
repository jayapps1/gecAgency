package com.ghanaemergencyconnect.agency

import com.ghanaemergencyconnect.agency.data.dto.StaffNotificationDto
import com.ghanaemergencyconnect.agency.data.mapper.toDomain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotificationWorkflowTest {

    @Test
    fun notificationDto_mapsToDomainCorrectly() {
        val dto = StaffNotificationDto(
            id = "notif-1",
            staffUserId = "user-1",
            reportId = "report-100",
            assignmentId = "asgn-200",
            eventType = "INCIDENT_ASSIGNED",
            title = "New Emergency Assignment",
            body = "Fire reported in your operational district",
            readAt = null,
            createdAt = "2026-10-24T04:00:00Z"
        )

        val domain = dto.toDomain()

        assertEquals("notif-1", domain.id)
        assertEquals("report-100", domain.reportId)
        assertEquals("New Emergency Assignment", domain.title)
        assertFalse(domain.isRead)
    }

    @Test
    fun notificationReadStatus_isReadEvaluation() {
        val unreadDto = StaffNotificationDto(
            id = "1", staffUserId = "s1", reportId = null, assignmentId = null,
            eventType = "ALERT", title = "T", body = "B", readAt = null, createdAt = "now"
        )
        val readDto = StaffNotificationDto(
            id = "2", staffUserId = "s1", reportId = null, assignmentId = null,
            eventType = "ALERT", title = "T", body = "B", readAt = "2026-10-24T04:10:00Z", createdAt = "now"
        )

        assertFalse(unreadDto.toDomain().isRead)
        assertTrue(readDto.toDomain().isRead)
    }
}
