package com.ghanaemergencyconnect.agency

import com.ghanaemergencyconnect.agency.data.domain.model.CitizenReviewItem
import org.junit.Assert.assertEquals
import org.junit.Test

class ReviewWorkflowTest {

    @Test
    fun citizenReviewItem_dataMapping() {
        val item = CitizenReviewItem(
            id = "rev-1",
            reportId = "rep-100",
            assignmentId = "asgn-100",
            agencyName = "Ghana National Fire Service",
            stationName = "Komenda Fire Station",
            rating = 5,
            feedback = "Prompt and professional response",
            createdAt = "2026-10-24T00:00:00Z"
        )

        assertEquals("rev-1", item.id)
        assertEquals(5, item.rating)
        assertEquals("Ghana National Fire Service", item.agencyName)
    }
}
