package com.ghanaemergencyconnect.agency.data.api

import com.ghanaemergencyconnect.agency.data.dto.CitizenReviewDto
import com.ghanaemergencyconnect.agency.data.dto.PageResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ReviewApi {

    @GET("api/v1/admin/reviews")
    suspend fun getAdminReviews(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<PageResponseDto<CitizenReviewDto>>

    @GET("api/v1/admin/reviews/summary")
    suspend fun getAdminReviewSummary(): Response<Map<String, Any>>
}
