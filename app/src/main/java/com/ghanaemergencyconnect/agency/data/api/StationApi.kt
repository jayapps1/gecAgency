package com.ghanaemergencyconnect.agency.data.api

import com.ghanaemergencyconnect.agency.data.dto.StationContactDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface StationApi {

    @GET("api/v1/admin/stations/{stationId}/contacts")
    suspend fun getStationContacts(
        @Path("stationId") stationId: String
    ): Response<List<StationContactDto>>
}
