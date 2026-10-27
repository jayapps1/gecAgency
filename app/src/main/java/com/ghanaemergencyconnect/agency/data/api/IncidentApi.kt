package com.ghanaemergencyconnect.agency.data.api

import com.ghanaemergencyconnect.agency.data.dto.AdminReportDetailsResponseDto
import com.ghanaemergencyconnect.agency.data.dto.AdminReportSummaryResponseDto
import com.ghanaemergencyconnect.agency.data.dto.AssignResponderRequest
import com.ghanaemergencyconnect.agency.data.dto.EligibleResponderDto
import com.ghanaemergencyconnect.agency.data.dto.PageResponseDto
import com.ghanaemergencyconnect.agency.data.dto.ReportAssignmentRequest
import com.ghanaemergencyconnect.agency.data.dto.ReportStatusUpdateRequest
import com.ghanaemergencyconnect.agency.data.dto.ReportTransferRequest
import com.ghanaemergencyconnect.agency.data.dto.ResponderAssignmentDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IncidentApi {

    @GET("api/v1/staff/incidents")
    suspend fun getStaffIncidents(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("sort") sort: String = "createdAt,desc"
    ): Response<PageResponseDto<AdminReportSummaryResponseDto>>

    @GET("api/v1/staff/incidents/{reportId}")
    suspend fun getStaffIncidentDetail(
        @Path("reportId") reportId: String
    ): Response<AdminReportDetailsResponseDto>

    @POST("api/v1/admin/reports/{reportId}/acknowledge")
    suspend fun acknowledgeReport(
        @Path("reportId") reportId: String
    ): Response<AdminReportDetailsResponseDto>

    @PATCH("api/v1/admin/reports/{reportId}/status")
    suspend fun updateReportStatus(
        @Path("reportId") reportId: String,
        @Body request: ReportStatusUpdateRequest
    ): Response<AdminReportDetailsResponseDto>

    @POST("api/v1/admin/reports/{reportId}/assign")
    suspend fun assignReport(
        @Path("reportId") reportId: String,
        @Body request: ReportAssignmentRequest
    ): Response<AdminReportDetailsResponseDto>

    @POST("api/v1/admin/reports/{reportId}/transfer")
    suspend fun transferReport(
        @Path("reportId") reportId: String,
        @Body request: ReportTransferRequest
    ): Response<AdminReportDetailsResponseDto>

    @GET("api/v1/admin/reports/{reportId}/eligible-responders")
    suspend fun getEligibleResponders(
        @Path("reportId") reportId: String
    ): Response<List<EligibleResponderDto>>

    @GET("api/v1/admin/reports/{reportId}/responders")
    suspend fun getAssignedResponders(
        @Path("reportId") reportId: String
    ): Response<List<ResponderAssignmentDto>>

    @POST("api/v1/admin/reports/{reportId}/responders")
    suspend fun assignResponder(
        @Path("reportId") reportId: String,
        @Body request: AssignResponderRequest
    ): Response<ResponderAssignmentDto>

    @DELETE("api/v1/admin/reports/{reportId}/responders/{assignmentId}")
    suspend fun releaseResponder(
        @Path("reportId") reportId: String,
        @Path("assignmentId") assignmentId: String
    ): Response<Void>

    @GET("api/v1/staff/me/assignments")
    suspend fun getMyActiveAssignments(): Response<List<ResponderAssignmentDto>>
}
