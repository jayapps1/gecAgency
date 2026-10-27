package com.ghanaemergencyconnect.agency.core.auth

import com.ghanaemergencyconnect.agency.data.domain.model.AuthenticatedStaff

/**
 * Single source of truth for global authentication/session state.
 */
sealed interface SessionState {
    data object Unknown : SessionState
    data object Unauthenticated : SessionState
    data class Authenticated(val staff: com.ghanaemergencyconnect.agency.data.domain.model.AuthenticatedStaff) : SessionState
    data object Expired : SessionState
}
