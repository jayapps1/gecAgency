package com.ghanaemergencyconnect.agency.data.repository

import com.ghanaemergencyconnect.agency.data.api.StationApi
import com.ghanaemergencyconnect.agency.data.domain.model.StationContactItem
import com.ghanaemergencyconnect.agency.data.domain.repository.StationRepository

class StationRepositoryImpl(
    private val stationApi: StationApi
) : StationRepository {

    override suspend fun getStationContacts(stationId: String): Result<List<StationContactItem>> {
        return try {
            val response = stationApi.getStationContacts(stationId)
            if (response.isSuccessful) {
                val list = response.body()?.map { dto ->
                    StationContactItem(
                        id = dto.id,
                        stationId = dto.stationId,
                        contactNumber = dto.contactNumber,
                        contactLabel = dto.contactLabel ?: "Station Contact",
                        priority = dto.priority,
                        verified = dto.verified
                    )
                } ?: emptyList()
                Result.success(list)
            } else {
                Result.failure(Exception("Failed to load station contacts."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
