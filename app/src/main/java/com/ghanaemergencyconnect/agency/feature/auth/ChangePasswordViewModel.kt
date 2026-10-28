package com.ghanaemergencyconnect.agency.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghanaemergencyconnect.agency.data.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    fun onCurrentPasswordChanged(value: String) {
        _uiState.update { it.copy(currentPassword = value, errorMessage = null) }
    }

    fun onNewPasswordChanged(value: String) {
        _uiState.update { it.copy(newPassword = value, errorMessage = null) }
    }

    fun onConfirmPasswordChanged(value: String) {
        _uiState.update { it.copy(confirmPassword = value, errorMessage = null) }
    }

    fun toggleCurrentVisibility() {
        _uiState.update { it.copy(isCurrentVisible = !it.isCurrentVisible) }
    }

    fun toggleNewVisibility() {
        _uiState.update { it.copy(isNewVisible = !it.isNewVisible) }
    }

    fun toggleConfirmVisibility() {
        _uiState.update { it.copy(isConfirmVisible = !it.isConfirmVisible) }
    }

    fun submitChangePassword() {
        val state = _uiState.value
        if (state.currentPassword.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Current password is required.") }
            return
        }
        if (state.newPassword.isBlank()) {
            _uiState.update { it.copy(errorMessage = "New password is required.") }
            return
        }
        if (state.newPassword != state.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "New password and confirmation do not match.") }
            return
        }
        if (state.newPassword == state.currentPassword) {
            _uiState.update { it.copy(errorMessage = "New password must be different from current password.") }
            return
        }

        _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
        viewModelScope.launch {
            val result = authRepository.changePassword(
                currentPassword = state.currentPassword,
                newPassword = state.newPassword
            )
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            isSuccess = true,
                            currentPassword = "",
                            newPassword = "",
                            confirmPassword = ""
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isSubmitting = false, errorMessage = error.message) }
                }
            )
        }
    }
}
