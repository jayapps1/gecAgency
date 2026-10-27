package com.ghanaemergencyconnect.agency.feature.auth

data class LoginUiState(
    val selectedMode: LoginMode = LoginMode.PASSWORD,
    val identifier: String = "",
    val password: String = "",
    val authenticatorCode: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)
