package com.ghanaemergencyconnect.agency.data.mapper

import com.ghanaemergencyconnect.agency.data.domain.model.EligibleResponder
import com.ghanaemergencyconnect.agency.data.domain.model.IncidentAssignmentStatus
import com.ghanaemergencyconnect.agency.data.domain.model.IncidentDetail
import com.ghanaemergencyconnect.agency.data.domain.model.OperationalIncident
import com.ghanaemergencyconnect.agency.data.domain.model.ResponderAssignment
import com.ghanaemergencyconnect.agency.data.dto.AdminReportDetailsResponseDto
import com.ghanaemergencyconnect.agency.data.dto.AdminReportSummaryResponseDto
import com.ghanaemergencyconnect.agency.data.dto.EligibleResponderDto
import com.ghanaemergencyconnect.agency.data.dto.ResponderAssignmentDto

fun AdminReportSummaryResponseDto.toDomain(): OperationalIncident {
    return OperationalIncident(
        reportId = id,
        publicReference = publicReference,
        categoryId = categoryId,
        categoryName = categoryName ?: "Incident",
        mode = mode,
        severity = severity,
        status = IncidentAssignmentStatus.fromCode(status),
        createdAt = createdAt ?: "",
        targetAgencyTypes = targetAgencyTypes ?: emptySet(),
        acknowledgementSlaState = acknowledgementSlaState ?: "ON_TIME",
        responseStartSlaState = responseStartSlaState ?: "ON_TIME"
    )
}

fun AdminReportDetailsResponseDto.toDomain(): IncidentDetail {
    return IncidentDetail(
        reportId = id,
        publicReference = publicReference,
        categoryId = categoryId,
        categoryName = categoryName ?: "Incident",
        mode = mode,
        severity = severity,
        status = IncidentAssignmentStatus.fromCode(status),
        description = description,
        latitude = latitude,
        longitude = longitude,
        locationAccuracyMeters = locationAccuracyMeters,
        landmark = landmark,
        locality = locality,
        district = district,
        region = region,
        vehicleRegistrationNumber = vehicleRegistrationNumber,
        vehicleType = vehicleType,
        vehicleColour = vehicleColour,
        directionOfTravel = directionOfTravel,
        createdAt = createdAt ?: "",
        updatedAt = updatedAt ?: "",
        targetAgencyTypes = targetAgencyTypes ?: emptySet(),
        statusHistory = statusHistory?.map { h ->
            IncidentDetail.StatusHistoryItem(
                status = IncidentAssignmentStatus.fromCode(h.status),
                note = h.note,
                timestamp = h.timestamp ?: ""
            )
        } ?: emptyList(),
        attachments = attachments?.map { att ->
            IncidentDetail.AttachmentItem(
                id = att.id,
                reportId = att.reportId,
                mediaType = att.mediaType,
                contentType = att.contentType,
                originalFileName = att.originalFileName,
                fileSizeBytes = att.fileSizeBytes,
                createdAt = att.createdAt ?: ""
            )
        } ?: emptyList(),
        agencyAssignments = agencyAssignments?.map { a ->
            IncidentDetail.AgencyAssignmentItem(
                assignmentId = a.assignmentId,
                agencyType = a.agencyType ?: "",
                agencyId = a.agencyId,
                agencyName = a.agencyName,
                stationId = a.stationId,
                stationName = a.stationName,
                status = IncidentAssignmentStatus.fromCode(a.status),
                assignedAt = a.assignedAt ?: "",
                updatedAt = a.updatedAt ?: "",
                acknowledgementSlaState = a.acknowledgementSlaState ?: "ON_TIME",
                responseStartSlaState = a.responseStartSlaState ?: "ON_TIME"
            )
        } ?: emptyList()
    )
}

fun ResponderAssignmentDto.toDomain(): ResponderAssignment {
    return ResponderAssignment(
        id = id,
        reportAssignmentId = reportAssignmentId,
        reportId = reportId,
        publicReference = publicReference,
        categoryName = categoryName,
        responderUserId = responderUserId,
        responderName = responderName ?: "Responder",
        responderRole = responderRole ?: "Responder",
        stationName = stationName,
        assignedAt = assignedAt,
        releasedAt = releasedAt,
        active = active
    )
}

fun EligibleResponderDto.toDomain(): EligibleResponder {
    return EligibleResponder(
        id = id,
        fullName = fullName,
        email = email,
        role = role,
        stationName = stationName,
        active = active,
        assigned = assigned
    )
}
