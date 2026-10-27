package com.ghanaemergencyconnect.agency.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghanaemergencyconnect.agency.core.auth.SessionState
import com.ghanaemergencyconnect.agency.data.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Unknown)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    fun initialize() {
        viewModelScope.launch {
            val state = authRepository.restoreSession()
            _sessionState.value = state
        }
    }
}
