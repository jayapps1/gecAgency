package com.ghanaemergencyconnect.agency.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghanaemergencyconnect.agency.data.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Owns transient staff sign-in form state.
 *
 * Identifier, password, and authenticator input remain in memory only;
 * authentication credentials are never written to persistent application storage.
 */
class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private var lastSubmittedCode: String? = null

    fun selectMode(mode: LoginMode) {
        _uiState.update {
            it.copy(
                selectedMode = mode,
                errorMessage = null
            )
        }
    }

    fun onIdentifierChanged(value: String) {
        _uiState.update { it.copy(identifier = value, errorMessage = null) }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun onAuthenticatorCodeChanged(value: String) {
        _uiState.update { it.copy(authenticatorCode = value, errorMessage = null) }

        if (value.length == 6 && !_uiState.value.isSubmitting && value != lastSubmittedCode) {
            submitAuthenticatorLogin()
        }
    }

    fun submitPasswordLogin() {
        val currentState = _uiState.value
        if (currentState.isSubmitting) return

        if (currentState.identifier.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Staff identifier / email is required.") }
            return
        }

        if (currentState.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Password is required.") }
            return
        }

        _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }

        viewModelScope.launch {
            val result = authRepository.loginPassword(
                identifier = currentState.identifier,
                password = currentState.password
            )

            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            password = "",
                            isSuccess = true
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            errorMessage = error.message ?: "Authentication failed."
                        )
                    }
                }
            )
        }
    }

    fun submitAuthenticatorLogin() {
        val currentState = _uiState.value
        if (currentState.isSubmitting) return

        if (currentState.identifier.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Staff identifier / email is required.") }
            return
        }

        if (currentState.authenticatorCode.length != 6) {
            _uiState.update { it.copy(errorMessage = "6-digit authenticator code is required.") }
            return
        }

        lastSubmittedCode = currentState.authenticatorCode
        _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }

        viewModelScope.launch {
            val result = authRepository.loginTotp(
                identifier = currentState.identifier,
                code = currentState.authenticatorCode
            )

            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            authenticatorCode = "",
                            isSuccess = true
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            errorMessage = error.message ?: "Incorrect or expired authenticator code."
                        )
                    }
                }
            )
        }
    }
}
