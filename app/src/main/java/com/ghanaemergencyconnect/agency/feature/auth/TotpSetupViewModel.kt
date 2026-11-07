package com.ghanaemergencyconnect.agency.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghanaemergencyconnect.agency.data.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TotpSetupViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TotpSetupUiState())
    val uiState: StateFlow<TotpSetupUiState> = _uiState.asStateFlow()

    fun startSetup() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val result = authRepository.startTotpSetup()
            result.fold(
                onSuccess = { setupData ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            manualSetupKey = setupData["manualSetupKey"] ?: setupData["secret"],
                            provisioningUri = setupData["provisioningUri"]
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
            )
        }
    }

    fun onCodeChanged(code: String) {
        _uiState.update { it.copy(codeInput = code, errorMessage = null) }
    }

    fun confirmSetup() {
        val code = _uiState.value.codeInput
        if (code.length != 6) {
            _uiState.update { it.copy(errorMessage = "Please enter the complete 6-digit verification code.") }
            return
        }

        _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
        viewModelScope.launch {
            val result = authRepository.confirmTotpSetup(code)
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isSubmitting = false, isSuccess = true, codeInput = "") }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isSubmitting = false, errorMessage = error.message) }
                }
            )
        }
    }
}
