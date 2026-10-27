package com.ghanaemergencyconnect.agency.data.domain.model

data class OperationalAppliance(
    val id: String,
    val agencyId: String,
    val homeStationId: String,
    val homeStationName: String?,
    val currentStationId: String?,
    val currentStationName: String?,
    val resourceType: String,
    val callSign: String,
    val registrationNumber: String?,
    val displayName: String,
    val status: com.ghanaemergencyconnect.agency.data.domain.model.ApplianceStatus,
    val statusReason: String?,
    val active: Boolean,
    val updatedAt: String
) {
    val effectiveStationName: String
        get() = currentStationName ?: homeStationName ?: "Assigned Station"

    val isRelocated: Boolean
        get() = !currentStationId.isNullOrBlank() && currentStationId != homeStationId
}
