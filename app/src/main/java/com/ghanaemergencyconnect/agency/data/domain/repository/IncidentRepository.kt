package com.ghanaemergencyconnect.agency.data.domain.repository

import com.ghanaemergencyconnect.agency.data.domain.model.EligibleResponder
import com.ghanaemergencyconnect.agency.data.domain.model.IncidentDetail
import com.ghanaemergencyconnect.agency.data.domain.model.OperationalIncident
import com.ghanaemergencyconnect.agency.data.domain.model.ResponderAssignment

interface IncidentRepository {
    suspend fun getStaffIncidents(page: Int = 0): Result<List<OperationalIncident>>
    suspend fun getStaffIncidentDetail(reportId: String): Result<IncidentDetail>
    suspend fun acknowledgeReport(reportId: String): Result<IncidentDetail>
    suspend fun startAction(reportId: String): Result<IncidentDetail>
    suspend fun resolveReport(reportId: String, note: String? = null): Result<IncidentDetail>

    suspend fun fetchEligibleResponders(reportId: String): Result<List<EligibleResponder>>
    suspend fun fetchAssignedResponders(reportId: String): Result<List<ResponderAssignment>>
    suspend fun assignResponder(reportId: String, responderUserId: String): Result<Unit>
    suspend fun releaseResponder(reportId: String, assignmentId: String): Result<Unit>
    suspend fun fetchMyActiveAssignments(): Result<List<ResponderAssignment>>
}
