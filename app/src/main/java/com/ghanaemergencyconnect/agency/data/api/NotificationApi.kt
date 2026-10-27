package com.ghanaemergencyconnect.agency.data.api

import com.ghanaemergencyconnect.agency.data.dto.PageResponseDto
import com.ghanaemergencyconnect.agency.data.dto.StaffDeviceRegistrationDto
import com.ghanaemergencyconnect.agency.data.dto.StaffNotificationDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApi {

    @POST("api/v1/staff/devices")
    suspend fun registerStaffDevice(
        @Body request: StaffDeviceRegistrationDto
    ): Response<Void>

    @DELETE("api/v1/staff/devices/{installationId}")
    suspend fun revokeStaffDevice(
        @Path("installationId") installationId: String
    ): Response<Void>

    @GET("api/v1/staff/notifications")
    suspend fun getStaffNotifications(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<PageResponseDto<StaffNotificationDto>>

    @GET("api/v1/staff/notifications/unread-count")
    suspend fun getUnreadNotificationCount(): Response<Map<String, Any>>

    @POST("api/v1/staff/notifications/{id}/read")
    suspend fun markNotificationAsRead(
        @Path("id") id: String
    ): Response<Void>

    @POST("api/v1/staff/notifications/read-all")
    suspend fun markAllNotificationsAsRead(): Response<Void>
}
