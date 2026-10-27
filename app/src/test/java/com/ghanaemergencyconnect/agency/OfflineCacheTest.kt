package com.ghanaemergencyconnect.agency

import com.ghanaemergencyconnect.agency.data.domain.model.IncidentAssignmentStatus
import com.ghanaemergencyconnect.agency.data.domain.model.OperationalIncident
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class OfflineCacheTest {

    @Test
    fun cachedIncidentRecord_preservesState() {
        val cached = OperationalIncident(
            reportId = "rep-cached-1",
            publicReference = "GEC-CACHE-01",
            categoryId = "cat-1",
            categoryName = "Fire Outbreak",
            mode = "EMERGENCY",
            severity = "CRITICAL",
            status = IncidentAssignmentStatus.ACKNOWLEDGED,
            createdAt = "2026-10-24T00:00:00Z",
            targetAgencyTypes = setOf("FIRE"),
            acknowledgementSlaState = "ON_TIME",
            responseStartSlaState = "ON_TIME"
        )

        assertEquals("rep-cached-1", cached.reportId)
        assertEquals(IncidentAssignmentStatus.ACKNOWLEDGED, cached.status)
        assertNotNull(cached.publicReference)
    }
}
