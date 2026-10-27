package com.ghanaemergencyconnect.agency.data.repository

import com.ghanaemergencyconnect.agency.core.security.SecureSessionManager
import com.ghanaemergencyconnect.agency.data.api.NotificationApi
import com.ghanaemergencyconnect.agency.data.dto.StaffDeviceRegistrationDto
import com.ghanaemergencyconnect.agency.data.mapper.toDomain
import com.ghanaemergencyconnect.agency.data.domain.model.StaffNotificationItem
import com.ghanaemergencyconnect.agency.data.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class NotificationRepositoryImpl(
    private val notificationApi: NotificationApi,
    private val sessionManager: SecureSessionManager
) : NotificationRepository {

    private val _unreadCount = MutableStateFlow(0)
    override val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    private val installationId: String by lazy {
        var id = sessionManager.getUserId()
        if (id.isNullOrBlank()) {
            id = UUID.randomUUID().toString()
        }
        id
    }

    override suspend fun registerDevice(pushToken: String): Result<Unit> {
        return try {
            val dto = StaffDeviceRegistrationDto(
                installationId = installationId,
                pushToken = pushToken,
                platform = "ANDROID",
                deviceLabel = "Android Staff Device",
                appVersion = "1.0.0"
            )
            val response = notificationApi.registerStaffDevice(dto)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to register device push token."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchNotifications(page: Int): Result<List<StaffNotificationItem>> {
        return try {
            val response = notificationApi.getStaffNotifications(page = page)
            if (response.isSuccessful) {
                val list = response.body()?.content?.map { it.toDomain() } ?: emptyList()
                fetchUnreadCount()
                Result.success(list)
            } else {
                Result.failure(Exception("Failed to load staff notifications."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchUnreadCount(): Result<Int> {
        return try {
            val response = notificationApi.getUnreadNotificationCount()
            if (response.isSuccessful) {
                val map = response.body()
                val count = (map?.get("unreadCount") as? Double)?.toInt() ?: 0
                _unreadCount.value = count
                Result.success(count)
            } else {
                Result.failure(Exception("Failed to load unread count."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markAsRead(id: String): Result<Unit> {
        return try {
            val response = notificationApi.markNotificationAsRead(id)
            if (response.isSuccessful) {
                fetchUnreadCount()
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to mark notification as read."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markAllAsRead(): Result<Unit> {
        return try {
            val response = notificationApi.markAllNotificationsAsRead()
            if (response.isSuccessful) {
                _unreadCount.value = 0
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to mark all as read."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
