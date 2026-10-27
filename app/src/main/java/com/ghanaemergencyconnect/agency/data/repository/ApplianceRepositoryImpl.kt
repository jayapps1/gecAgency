package com.ghanaemergencyconnect.agency.data.repository

import com.ghanaemergencyconnect.agency.data.api.ApplianceApi
import com.ghanaemergencyconnect.agency.data.mapper.toDomain
import com.ghanaemergencyconnect.agency.data.domain.model.OperationalAppliance
import com.ghanaemergencyconnect.agency.data.domain.repository.ApplianceRepository

class ApplianceRepositoryImpl(
    private val applianceApi: ApplianceApi
) : ApplianceRepository {

    override suspend fun fetchAppliances(): Result<List<OperationalAppliance>> {
        return try {
            val response = applianceApi.getAdminAppliances()
            if (response.isSuccessful) {
                val list = response.body()?.map { it.toDomain() } ?: emptyList()
                Result.success(list)
            } else {
                Result.failure(Exception("Failed to load appliance fleet."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateStatus(id: String, status: String, reason: String?): Result<OperationalAppliance> {
        return try {
            val map = mutableMapOf<String, String>("status" to status)
            if (!reason.isNullOrBlank()) {
                map["reason"] = reason
            }
            val response = applianceApi.updateApplianceStatus(id, map)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Failed to update appliance status."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun relocate(id: String, newStationId: String): Result<OperationalAppliance> {
        return try {
            val map = mapOf("newStationId" to newStationId)
            val response = applianceApi.relocateAppliance(id, map)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Failed to relocate appliance."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun returnHome(id: String): Result<OperationalAppliance> {
        return try {
            val response = applianceApi.returnToHomeStation(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(Exception("Failed to return appliance to home station."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
