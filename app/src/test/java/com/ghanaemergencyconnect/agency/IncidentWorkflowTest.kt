package com.ghanaemergencyconnect.agency

import com.ghanaemergencyconnect.agency.data.dto.AdminReportDetailsResponseDto
import com.ghanaemergencyconnect.agency.data.dto.AdminReportSummaryResponseDto
import com.ghanaemergencyconnect.agency.data.mapper.toDomain
import com.ghanaemergencyconnect.agency.data.domain.model.IncidentAssignmentStatus
import com.ghanaemergencyconnect.agency.feature.incidents.IncidentsFilter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class IncidentWorkflowTest {

    @Test
    fun incidentAssignmentStatus_parseCodeCorrectly() {
        assertEquals(IncidentAssignmentStatus.ACKNOWLEDGED, IncidentAssignmentStatus.fromCode("ACKNOWLEDGED"))
        assertEquals(IncidentAssignmentStatus.IN_PROGRESS, IncidentAssignmentStatus.fromCode("IN_PROGRESS"))
        assertEquals(IncidentAssignmentStatus.RESOLVED, IncidentAssignmentStatus.fromCode("RESOLVED"))
        assertEquals(IncidentAssignmentStatus.UNKNOWN, IncidentAssignmentStatus.fromCode("INVALID"))
    }

    @Test
    fun incidentAssignmentStatus_terminalStateDetection() {
        assertTrue(IncidentAssignmentStatus.RESOLVED.isTerminal)
        assertTrue(IncidentAssignmentStatus.CLOSED.isTerminal)
        assertFalse(IncidentAssignmentStatus.IN_PROGRESS.isTerminal)
        assertFalse(IncidentAssignmentStatus.ACKNOWLEDGED.isTerminal)
    }

    @Test
    fun incidentsFilter_activeFilterMatching() {
        assertTrue(IncidentsFilter.ALL_ACTIVE.matches(IncidentAssignmentStatus.RECEIVED))
        assertTrue(IncidentsFilter.ALL_ACTIVE.matches(IncidentAssignmentStatus.ACKNOWLEDGED))
        assertTrue(IncidentsFilter.ALL_ACTIVE.matches(IncidentAssignmentStatus.IN_PROGRESS))
        assertFalse(IncidentsFilter.ALL_ACTIVE.matches(IncidentAssignmentStatus.RESOLVED))
    }

    @Test
    fun incidentSummaryMapper_mapsDtoToDomain() {
        val dto = AdminReportSummaryResponseDto(
            id = "rep-123",
            publicReference = "GEC-2026-001",
            categoryId = "cat-1",
            categoryName = "Fire Outbreak",
            mode = "EMERGENCY",
            severity = "CRITICAL",
            status = "ACKNOWLEDGED",
            createdAt = "2026-10-24T00:00:00Z",
            targetAgencyTypes = setOf("FIRE"),
            acknowledgementSlaState = "ON_TIME",
            responseStartSlaState = "ON_TIME"
        )

        val domain = dto.toDomain()

        assertEquals("rep-123", domain.reportId)
        assertEquals("GEC-2026-001", domain.publicReference)
        assertEquals("Fire Outbreak", domain.categoryName)
        assertEquals(IncidentAssignmentStatus.ACKNOWLEDGED, domain.status)
        assertEquals("ON_TIME", domain.acknowledgementSlaState)
    }

    @Test
    fun incidentDetailMapper_findsMyAgencyAssignment() {
        val dto = AdminReportDetailsResponseDto(
            id = "rep-123",
            publicReference = "GEC-2026-001",
            categoryId = "cat-1",
            categoryName = "Fire Outbreak",
            mode = "EMERGENCY",
            severity = "CRITICAL",
            status = "IN_PROGRESS",
            description = "Building fire reported",
            latitude = 5.6037,
            longitude = -0.1870,
            locationAccuracyMeters = 10.0,
            landmark = "Near Market",
            locality = "Accra",
            district = "Accra Metro",
            region = "Greater Accra",
            vehicleRegistrationNumber = null,
            vehicleType = null,
            vehicleColour = null,
            directionOfTravel = null,
            createdAt = "2026-10-24T00:00:00Z",
            updatedAt = "2026-10-24T00:05:00Z",
            targetAgencyTypes = setOf("FIRE", "POLICE"),
            statusHistory = emptyList(),
            attachments = emptyList(),
            agencyAssignments = listOf(
                AdminReportDetailsResponseDto.AgencyAssignmentDto(
                    assignmentId = "asgn-fire",
                    agencyType = "FIRE",
                    agencyId = "agency-fire-id",
                    agencyName = "GNFS",
                    stationId = "station-1",
                    stationName = "Accra Central",
                    status = "IN_PROGRESS",
                    assignedAt = "2026-10-24T00:01:00Z",
                    updatedAt = "2026-10-24T00:05:00Z",
                    acknowledgementSlaState = "ON_TIME",
                    responseStartSlaState = "ON_TIME"
                )
            )
        )

        val domain = dto.toDomain()

        val myAssignment = domain.findMyAgencyAssignment("agency-fire-id")

        assertEquals("asgn-fire", myAssignment?.assignmentId)
        assertEquals("GNFS", myAssignment?.agencyName)
        assertEquals(IncidentAssignmentStatus.IN_PROGRESS, myAssignment?.status)
    }
}
