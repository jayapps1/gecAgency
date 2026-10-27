package com.ghanaemergencyconnect.agency.feature.notifications

import com.ghanaemergencyconnect.agency.data.domain.model.StaffNotificationItem

data class NotificationsUiState(
    val notifications: List<StaffNotificationItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
