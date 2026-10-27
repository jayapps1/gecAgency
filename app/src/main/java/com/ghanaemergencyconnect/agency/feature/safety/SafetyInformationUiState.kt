package com.ghanaemergencyconnect.agency.feature.safety

import com.ghanaemergencyconnect.agency.data.domain.model.SafetyInformationItem

data class SafetyInformationUiState(
    val posts: List<SafetyInformationItem> = emptyList(),
    val isLoading: Boolean = false,
    val isPublishing: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
