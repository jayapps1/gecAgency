package com.ghanaemergencyconnect.agency.data.mapper

import com.ghanaemergencyconnect.agency.data.domain.model.ApplianceStatus
import com.ghanaemergencyconnect.agency.data.domain.model.OperationalAppliance
import com.ghanaemergencyconnect.agency.data.dto.ResponseApplianceDto

fun ResponseApplianceDto.toDomain(): OperationalAppliance {
    return OperationalAppliance(
        id = id,
        agencyId = agencyId,
        homeStationId = homeStationId,
        homeStationName = homeStationName,
        currentStationId = currentStationId,
        currentStationName = currentStationName,
        resourceType = resourceType ?: "VEHICLE",
        callSign = callSign,
        registrationNumber = registrationNumber,
        displayName = displayName ?: callSign,
        status = ApplianceStatus.fromCode(operationalStatus),
        statusReason = statusReason,
        active = active,
        updatedAt = updatedAt ?: ""
    )
}
