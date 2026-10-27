package com.ghanaemergencyconnect.agency.data.api

import com.ghanaemergencyconnect.agency.data.dto.ApplianceStatusHistoryDto
import com.ghanaemergencyconnect.agency.data.dto.ResponseApplianceDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApplianceApi {

    @GET("api/v1/admin/appliances")
    suspend fun getAdminAppliances(): Response<List<ResponseApplianceDto>>

    @GET("api/v1/admin/appliances/summary")
    suspend fun getAgencyApplianceSummary(): Response<Map<String, Any>>

    @POST("api/v1/admin/appliances/{id}/status")
    suspend fun updateApplianceStatus(
        @Path("id") id: String,
        @Body body: Map<String, String>
    ): Response<ResponseApplianceDto>

    @POST("api/v1/admin/appliances/{id}/relocate")
    suspend fun relocateAppliance(
        @Path("id") id: String,
        @Body body: Map<String, String>
    ): Response<ResponseApplianceDto>

    @POST("api/v1/admin/appliances/{id}/return-home")
    suspend fun returnToHomeStation(
        @Path("id") id: String
    ): Response<ResponseApplianceDto>

    @POST("api/v1/admin/appliances/{id}/deactivate")
    suspend fun deactivateAppliance(
        @Path("id") id: String
    ): Response<ResponseApplianceDto>

    @GET("api/v1/admin/appliances/{id}/history")
    suspend fun getApplianceHistory(
        @Path("id") id: String
    ): Response<List<ApplianceStatusHistoryDto>>
}
