package com.ghanaemergencyconnect.agency.data.mapper

import com.ghanaemergencyconnect.agency.data.domain.model.AuthenticatedStaff
import com.ghanaemergencyconnect.agency.data.dto.AdminUserResponseDto

fun AdminUserResponseDto.toDomain(): AuthenticatedStaff {
    return AuthenticatedStaff(
        id = id,
        email = email,
        firstName = firstName ?: "",
        lastName = lastName ?: "",
        phoneNumber = phoneNumber,
        agencyId = agencyId,
        agencyName = agencyName,
        region = region,
        stationId = stationId,
        stationName = stationName,
        active = active,
        locked = locked,
        mustChangePassword = mustChangePassword,
        roles = roles ?: emptySet()
    )
}
