package com.ghanaemergencyconnect.agency.core.realtime

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

enum class InvalidationTarget {
    INCIDENTS,
    INCIDENT_DETAIL,
    APPLIANCES,
    NOTIFICATIONS
}

object RealtimeInvalidationBus {
    private val _events = MutableSharedFlow<RealtimeEvent>(extraBufferCapacity = 64)
    val events: SharedFlow<RealtimeEvent> = _events.asSharedFlow()

    private val _invalidations = MutableSharedFlow<InvalidationTarget>(extraBufferCapacity = 64)
    val invalidations: SharedFlow<InvalidationTarget> = _invalidations.asSharedFlow()

    fun emitEvent(event: RealtimeEvent) {
        _events.tryEmit(event)
        when (event.name) {
            "INCIDENT_ASSIGNED", "STATUS_CHANGED", "REPORT_UPDATED" -> {
                _invalidations.tryEmit(InvalidationTarget.INCIDENTS)
                _invalidations.tryEmit(InvalidationTarget.INCIDENT_DETAIL)
            }
            "APPLIANCE_STATUS_CHANGED", "APPLIANCE_RELOCATED" -> {
                _invalidations.tryEmit(InvalidationTarget.APPLIANCES)
            }
            "STAFF_NOTIFICATION_CREATED" -> {
                _invalidations.tryEmit(InvalidationTarget.NOTIFICATIONS)
            }
        }
    }

    fun triggerInvalidation(target: InvalidationTarget) {
        _invalidations.tryEmit(target)
    }
}
