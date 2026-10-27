package com.ghanaemergencyconnect.agency.data.api

import com.ghanaemergencyconnect.agency.data.dto.CreateSafetyInformationRequestDto
import com.ghanaemergencyconnect.agency.data.dto.PageResponseDto
import com.ghanaemergencyconnect.agency.data.dto.SafetyInformationDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SafetyApi {

    @GET("api/v1/admin/safety-information")
    suspend fun getAdminPosts(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<PageResponseDto<SafetyInformationDto>>

    @POST("api/v1/admin/safety-information")
    suspend fun createPost(
        @Body request: CreateSafetyInformationRequestDto
    ): Response<Map<String, Any>>

    @POST("api/v1/admin/safety-information/{id}/publish")
    suspend fun publishPost(
        @Path("id") id: String
    ): Response<Map<String, Any>>
}
