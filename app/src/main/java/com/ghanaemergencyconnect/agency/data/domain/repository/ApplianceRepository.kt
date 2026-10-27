package com.ghanaemergencyconnect.agency.data.domain.repository

import com.ghanaemergencyconnect.agency.data.domain.model.OperationalAppliance

interface ApplianceRepository {
    suspend fun fetchAppliances(): Result<List<com.ghanaemergencyconnect.agency.data.domain.model.OperationalAppliance>>
    suspend fun updateStatus(id: String, status: String, reason: String? = null): Result<com.ghanaemergencyconnect.agency.data.domain.model.OperationalAppliance>
    suspend fun relocate(id: String, newStationId: String): Result<com.ghanaemergencyconnect.agency.data.domain.model.OperationalAppliance>
    suspend fun returnHome(id: String): Result<com.ghanaemergencyconnect.agency.data.domain.model.OperationalAppliance>
}
