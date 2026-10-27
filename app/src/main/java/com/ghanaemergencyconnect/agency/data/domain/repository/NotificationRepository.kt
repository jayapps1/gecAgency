package com.ghanaemergencyconnect.agency.data.domain.repository

import com.ghanaemergencyconnect.agency.data.domain.model.StaffNotificationItem
import kotlinx.coroutines.flow.StateFlow

interface NotificationRepository {
    val unreadCount: StateFlow<Int>

    suspend fun registerDevice(pushToken: String): Result<Unit>
    suspend fun fetchNotifications(page: Int = 0): Result<List<com.ghanaemergencyconnect.agency.data.domain.model.StaffNotificationItem>>
    suspend fun fetchUnreadCount(): Result<Int>
    suspend fun markAsRead(id: String): Result<Unit>
    suspend fun markAllAsRead(): Result<Unit>
}
