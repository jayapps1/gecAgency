package com.ghanaemergencyconnect.agency.data.repository

import com.ghanaemergencyconnect.agency.core.auth.SessionState
import com.ghanaemergencyconnect.agency.core.security.SecureSessionManager
import com.ghanaemergencyconnect.agency.data.api.AuthApi
import com.ghanaemergencyconnect.agency.data.dto.LoginRequestDto
import com.ghanaemergencyconnect.agency.data.dto.TotpLoginRequestDto
import com.ghanaemergencyconnect.agency.data.mapper.toDomain
import com.ghanaemergencyconnect.agency.data.domain.model.AuthenticatedStaff
import com.ghanaemergencyconnect.agency.data.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val sessionManager: SecureSessionManager
) : com.ghanaemergencyconnect.agency.data.domain.repository.AuthRepository {

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Unknown)
    override val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    override suspend fun loginPassword(identifier: String, password: String): Result<Unit> {
        return try {
            val response = authApi.loginPassword(LoginRequestDto(email = identifier.trim(), password = password))
            if (response.isSuccessful) {
                val body = response.body()
                val token = body?.accessToken
                val userDto = body?.user
                if (!token.isNullOrBlank() && userDto != null) {
                    val staff = userDto.toDomain()
                    sessionManager.saveTokens(token, null)
                    sessionManager.saveStaffData(
                        userId = staff.id,
                        email = staff.email,
                        firstName = staff.firstName,
                        lastName = staff.lastName,
                        agencyId = staff.agencyId,
                        region = staff.region,
                        stationId = staff.stationId,
                        roles = staff.roles
                    )
                    _sessionState.value = SessionState.Authenticated(staff)
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Authentication failed: Empty token or user details."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Unable to connect to Ghana Emergency Connect. Check your connection and try again."))
        }
    }

    override suspend fun loginTotp(identifier: String, code: String): Result<Unit> {
        return try {
            val response = authApi.loginTotp(TotpLoginRequestDto(email = identifier.trim(), code = code.trim()))
            if (response.isSuccessful) {
                val body = response.body()
                val token = body?.accessToken
                val userDto = body?.user
                if (!token.isNullOrBlank() && userDto != null) {
                    val staff = userDto.toDomain()
                    sessionManager.saveTokens(token, null)
                    sessionManager.saveStaffData(
                        userId = staff.id,
                        email = staff.email,
                        firstName = staff.firstName,
                        lastName = staff.lastName,
                        agencyId = staff.agencyId,
                        region = staff.region,
                        stationId = staff.stationId,
                        roles = staff.roles
                    )
                    _sessionState.value = SessionState.Authenticated(staff)
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Authentication failed: Incorrect or expired authenticator code."))
                }
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string())
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Unable to connect to Ghana Emergency Connect. Check your connection and try again."))
        }
    }

    override suspend fun restoreSession(): SessionState {
        val token = sessionManager.getAccessToken()
        val userId = sessionManager.getUserId()
        val email = sessionManager.getEmail()

        if (token.isNullOrBlank() || userId.isNullOrBlank() || email.isNullOrBlank()) {
            _sessionState.value = SessionState.Unauthenticated
            return SessionState.Unauthenticated
        }

        return try {
            val response = authApi.getStaffProfile()
            if (response.isSuccessful && response.body() != null) {
                val staff = response.body()!!.toDomain()
                _sessionState.value = SessionState.Authenticated(staff)
                SessionState.Authenticated(staff)
            } else {
                sessionManager.clearSession()
                _sessionState.value = SessionState.Unauthenticated
                SessionState.Unauthenticated
            }
        } catch (e: Exception) {
            // Safe offline fallback when saved session exists
            val staff = AuthenticatedStaff(
                id = userId,
                email = email,
                firstName = sessionManager.getFirstName() ?: "",
                lastName = sessionManager.getLastName() ?: "",
                phoneNumber = null,
                agencyId = sessionManager.getAgencyId(),
                agencyName = null,
                region = sessionManager.getRegion(),
                stationId = sessionManager.getStationId(),
                stationName = null,
                active = true,
                locked = false,
                mustChangePassword = false,
                roles = sessionManager.getRoles()
            )
            _sessionState.value = SessionState.Authenticated(staff)
            SessionState.Authenticated(staff)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            authApi.logout()
            sessionManager.clearSession()
            _sessionState.value = SessionState.Unauthenticated
            Result.success(Unit)
        } catch (e: Exception) {
            sessionManager.clearSession()
            _sessionState.value = SessionState.Unauthenticated
            Result.success(Unit)
        }
    }

    private fun parseErrorMessage(errorBody: String?): String {
        if (errorBody.isNullOrBlank()) return "Authentication failed. Please try again."
        return try {
            val json = JSONObject(errorBody)
            json.optString("message", "Authentication failed. Please try again.")
        } catch (e: Exception) {
            "Authentication failed. Please try again."
        }
    }
}
