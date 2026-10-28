package com.ghanaemergencyconnect.agency.feature.auth

data class ChangePasswordUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isCurrentVisible: Boolean = false,
    val isNewVisible: Boolean = false,
    val isConfirmVisible: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)
