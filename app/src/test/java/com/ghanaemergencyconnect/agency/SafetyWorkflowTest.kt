package com.ghanaemergencyconnect.agency

import com.ghanaemergencyconnect.agency.data.dto.SafetyInformationDto
import com.ghanaemergencyconnect.agency.data.domain.model.SafetyInformationItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class SafetyWorkflowTest {

    @Test
    fun safetyInformationItem_dataMapping() {
        val item = SafetyInformationItem(
            id = "post-100",
            agencyId = "agency-gnfs",
            title = "Harmattan Fire Advisory",
            content = "Keep clear of dry bush areas during high wind advisories.",
            category = "FIRE_SAFETY",
            severity = "WARNING",
            status = "PUBLISHED",
            notifyUsers = true,
            publishedAt = "2026-10-24T00:00:00Z",
            createdAt = "2026-10-24T00:00:00Z"
        )

        assertEquals("post-100", item.id)
        assertEquals("Harmattan Fire Advisory", item.title)
        assertEquals("PUBLISHED", item.status)
        assertNotNull(item.publishedAt)
    }

    @Test
    fun safetyInformationDto_modelFields() {
        val dto = SafetyInformationDto(
            id = "p-1",
            sourceType = "AGENCY",
            agencyId = "a-1",
            title = "Advisory",
            summary = "Summary",
            content = "Content",
            category = "GENERAL",
            severity = "INFO",
            status = "DRAFT",
            notifyUsers = false,
            publishedAt = null,
            expiresAt = null,
            createdAt = "now"
        )

        assertEquals("p-1", dto.id)
        assertEquals("DRAFT", dto.status)
    }
}
