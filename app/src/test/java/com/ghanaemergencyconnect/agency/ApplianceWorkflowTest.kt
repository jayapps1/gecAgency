package com.ghanaemergencyconnect.agency

import com.ghanaemergencyconnect.agency.data.domain.model.ApplianceStatus
import com.ghanaemergencyconnect.agency.data.dto.ResponseApplianceDto
import com.ghanaemergencyconnect.agency.data.mapper.toDomain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ApplianceWorkflowTest {

    @Test
    fun applianceStatus_parseCodesAndLegacyValues() {
        assertEquals(ApplianceStatus.AVAILABLE, ApplianceStatus.fromCode("AVAILABLE"))
        assertEquals(ApplianceStatus.DISPATCHED, ApplianceStatus.fromCode("DISPATCHED"))
        assertEquals(ApplianceStatus.DISPATCHED, ApplianceStatus.fromCode("EN_ROUTE"))
        assertEquals(ApplianceStatus.OUT_OF_COMMISSION, ApplianceStatus.fromCode("OUT_OF_COMMISSION"))
        assertEquals(ApplianceStatus.MAINTENANCE, ApplianceStatus.fromCode("MAINTENANCE"))
        assertEquals(ApplianceStatus.UNKNOWN, ApplianceStatus.fromCode("INVALID_STATUS"))
    }

    @Test
    fun applianceRelocation_detectsRelocatedStation() {
        val dto = ResponseApplianceDto(
            id = "app-1",
            agencyId = "agency-1",
            homeStationId = "home-stn-1",
            homeStationName = "Komenda Fire Station",
            currentStationId = "curr-stn-2",
            currentStationName = "Elmina Fire Station",
            resourceType = "FIRE_ENGINE",
            callSign = "KNUTFORD 4",
            registrationNumber = "GN-100-24",
            displayName = "Water Tender",
            operationalStatus = "AVAILABLE",
            statusReason = null,
            active = true,
            updatedAt = "2026-10-24T04:00:00Z"
        )

        val domain = dto.toDomain()

        assertEquals("KNUTFORD 4", domain.callSign)
        assertEquals("Elmina Fire Station", domain.effectiveStationName)
        assertTrue(domain.isRelocated)
    }

    @Test
    fun applianceStation_sameStationNotRelocated() {
        val dto = ResponseApplianceDto(
            id = "app-1",
            agencyId = "agency-1",
            homeStationId = "home-stn-1",
            homeStationName = "Komenda Fire Station",
            currentStationId = "home-stn-1",
            currentStationName = "Komenda Fire Station",
            resourceType = "FIRE_ENGINE",
            callSign = "KNUTFORD 4",
            registrationNumber = null,
            displayName = "Water Tender",
            operationalStatus = "AVAILABLE",
            statusReason = null,
            active = true,
            updatedAt = "2026-10-24T04:00:00Z"
        )

        val domain = dto.toDomain()

        assertFalse(domain.isRelocated)
    }
}
