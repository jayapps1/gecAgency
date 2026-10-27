package com.ghanaemergencyconnect.agency.feature.splash

/**
 * Startup initialization state.
 * Represents application startup lifecycle state without artificial delays.
 */
sealed interface SplashUiState {
    data object Loading : SplashUiState
    data object Ready : SplashUiState
}
