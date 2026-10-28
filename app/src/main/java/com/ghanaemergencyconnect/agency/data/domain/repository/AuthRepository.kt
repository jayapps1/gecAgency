package com.ghanaemergencyconnect.agency.data.domain.repository

import com.ghanaemergencyconnect.agency.core.auth.SessionState
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val sessionState: StateFlow<SessionState>

    suspend fun loginPassword(identifier: String, password: String): Result<Unit>
    suspend fun loginTotp(identifier: String, code: String): Result<Unit>
    suspend fun restoreSession(): SessionState
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit>
    suspend fun logout(): Result<Unit>
}
