package com.ghanaemergencyconnect.agency

import com.ghanaemergencyconnect.agency.core.realtime.InvalidationTarget
import com.ghanaemergencyconnect.agency.core.realtime.RealtimeEvent
import com.ghanaemergencyconnect.agency.core.realtime.RealtimeInvalidationBus
import com.ghanaemergencyconnect.agency.core.util.SlaTimerUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class RealtimeSseClientTest {

    @Test
    fun realtimeEvent_modelDataHandling() {
        val event = RealtimeEvent(
            name = "INCIDENT_ASSIGNED",
            reportId = "rep-100",
            assignmentId = "asgn-200",
            message = "New incident assignment"
        )

        assertEquals("INCIDENT_ASSIGNED", event.name)
        assertEquals("rep-100", event.reportId)
        assertEquals("asgn-200", event.assignmentId)
    }

    @Test
    fun realtimeInvalidationBus_emitsTargetInvalidations() {
        RealtimeInvalidationBus.triggerInvalidation(InvalidationTarget.INCIDENTS)
        assertNotNull(RealtimeInvalidationBus.invalidations)
    }

    @Test
    fun slaTimerUtils_formatElapsedTime() {
        val formatted = SlaTimerUtils.formatElapsedTime("2026-10-24T00:00:00Z")
        assertNotNull(formatted)
    }

    @Test
    fun slaTimerUtils_badgeLabels() {
        assertEquals("On Time", SlaTimerUtils.getSlaBadgeLabel("ON_TIME"))
        assertEquals("Attention", SlaTimerUtils.getSlaBadgeLabel("ATTENTION"))
        assertEquals("Delayed", SlaTimerUtils.getSlaBadgeLabel("DELAYED"))
    }
}
