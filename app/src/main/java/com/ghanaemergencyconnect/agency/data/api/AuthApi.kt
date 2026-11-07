package com.ghanaemergencyconnect.agency.data.api

import com.ghanaemergencyconnect.agency.data.dto.AdminUserResponseDto
import com.ghanaemergencyconnect.agency.data.dto.ChangePasswordRequestDto
import com.ghanaemergencyconnect.agency.data.dto.LoginRequestDto
import com.ghanaemergencyconnect.agency.data.dto.LoginResponseDto
import com.ghanaemergencyconnect.agency.data.dto.TotpLoginRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("api/v1/admin/auth/login")
    suspend fun loginPassword(
        @Body request: LoginRequestDto
    ): Response<LoginResponseDto>

    @POST("api/v1/admin/auth/login/totp-direct")
    suspend fun loginTotp(
        @Body request: TotpLoginRequestDto
    ): Response<LoginResponseDto>

    @POST("api/v1/admin/auth/refresh")
    suspend fun refreshSession(): Response<LoginResponseDto>

    @POST("api/v1/admin/auth/logout")
    suspend fun logout(): Response<Void>

    @GET("api/v1/staff/me")
    suspend fun getStaffProfile(): Response<AdminUserResponseDto>

    @POST("api/v1/admin/auth/change-password")
    suspend fun changePassword(
        @Body request: ChangePasswordRequestDto
    ): Response<Void>

    @POST("api/v1/admin/auth/mfa/totp/setup")
    suspend fun startTotpSetup(): Response<Map<String, String>>

    @POST("api/v1/admin/auth/mfa/totp/confirm")
    suspend fun confirmTotpSetup(
        @Body body: Map<String, String>
    ): Response<Map<String, Any>>

    @POST("api/v1/admin/auth/mfa/totp/disable")
    suspend fun disableTotp(
        @Body body: Map<String, String>
    ): Response<Map<String, String>>
}
