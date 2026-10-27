package com.ghanaemergencyconnect.agency.data.domain.repository

import com.ghanaemergencyconnect.agency.data.domain.model.StationContactItem

interface StationRepository {
    suspend fun getStationContacts(stationId: String): Result<List<com.ghanaemergencyconnect.agency.data.domain.model.StationContactItem>>
}
