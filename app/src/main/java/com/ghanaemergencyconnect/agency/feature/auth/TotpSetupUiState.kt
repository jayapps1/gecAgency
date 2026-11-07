package com.ghanaemergencyconnect.agency.feature.auth

data class TotpSetupUiState(
    val manualSetupKey: String? = null,
    val provisioningUri: String? = null,
    val codeInput: String = "",
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)
