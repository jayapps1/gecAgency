package com.ghanaemergencyconnect.agency.data.repository

import com.ghanaemergencyconnect.agency.core.network.ConnectivityObserver
import com.ghanaemergencyconnect.agency.data.api.IncidentApi
import com.ghanaemergencyconnect.agency.data.domain.model.EligibleResponder
import com.ghanaemergencyconnect.agency.data.domain.model.IncidentDetail
import com.ghanaemergencyconnect.agency.data.domain.model.OperationalIncident
import com.ghanaemergencyconnect.agency.data.domain.model.ResponderAssignment
import com.ghanaemergencyconnect.agency.data.domain.repository.IncidentRepository
import com.ghanaemergencyconnect.agency.data.dto.AssignResponderRequest
import com.ghanaemergencyconnect.agency.data.dto.ReportStatusUpdateRequest
import com.ghanaemergencyconnect.agency.data.mapper.toDomain
import org.json.JSONObject

/**
 * Exposes the last authorized incident state immediately from local storage
 * and reconciles it with the backend when connectivity is available.
 *
 * Cached records are never treated as authoritative for operational mutations.
 */
class IncidentRepositoryImpl(
    private val incidentApi: IncidentApi,
    private val connectivityObserver: ConnectivityObserver? = null
) : IncidentRepository {

    private var cachedIncidents: List<OperationalIncident> = emptyList()
    private val cachedDetails = mutableMapOf<String, IncidentDetail>()

    fun clearCache() {
        cachedIncidents = emptyList()
        cachedDetails.clear()
    }

    override suspend fun getStaffIncidents(page: Int): Result<List<OperationalIncident>> {
        if (connectivityObserver != null && !connectivityObserver.isOnline) {
            return if (cachedIncidents.isNotEmpty()) {
                Result.success(cachedIncidents)
            } else {
                Result.failure(Exception("Offline — no cached incident records available."))
            }
        }

        return try {
            val response = incidentApi.getStaffIncidents(page = page)
            if (response.isSuccessful) {
                val pageDto = response.body()
                val items = pageDto?.content?.map { it.toDomain() } ?: emptyList()
                cachedIncidents = items
                Result.success(items)
            } else {
                if (cachedIncidents.isNotEmpty()) {
                    Result.success(cachedIncidents)
                } else {
                    Result.failure(Exception(parseError(response.errorBody()?.string())))
                }
            }
        } catch (e: Exception) {
            if (cachedIncidents.isNotEmpty()) {
                Result.success(cachedIncidents)
            } else {
                Result.failure(Exception("Unable to connect to Ghana Emergency Connect. Check network and try again."))
            }
        }
    }

    override suspend fun getStaffIncidentDetail(reportId: String): Result<IncidentDetail> {
        if (connectivityObserver != null && !connectivityObserver.isOnline) {
            val cached = cachedDetails[reportId]
            return if (cached != null) {
                Result.success(cached)
            } else {
                Result.failure(Exception("Offline — no cached detail available for this incident."))
            }
        }

        return try {
            val response = incidentApi.getStaffIncidentDetail(reportId)
            if (response.isSuccessful && response.body() != null) {
                val detail = response.body()!!.toDomain()
                cachedDetails[reportId] = detail
                Result.success(detail)
            } else {
                val cached = cachedDetails[reportId]
                if (cached != null) {
                    Result.success(cached)
                } else {
                    Result.failure(Exception(parseError(response.errorBody()?.string())))
                }
            }
        } catch (e: Exception) {
            val cached = cachedDetails[reportId]
            if (cached != null) {
                Result.success(cached)
            } else {
                Result.failure(Exception("Unable to connect to Ghana Emergency Connect. Check network and try again."))
            }
        }
    }

    override suspend fun acknowledgeReport(reportId: String): Result<IncidentDetail> {
        if (connectivityObserver != null && !connectivityObserver.isOnline) {
            return Result.failure(Exception("Connection required for operational actions."))
        }

        return try {
            val response = incidentApi.acknowledgeReport(reportId)
            if (response.isSuccessful && response.body() != null) {
                val detail = response.body()!!.toDomain()
                cachedDetails[reportId] = detail
                Result.success(detail)
            } else {
                Result.failure(Exception(parseError(response.errorBody()?.string())))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Unable to complete acknowledge action. Check network and try again."))
        }
    }

    override suspend fun startAction(reportId: String): Result<IncidentDetail> {
        if (connectivityObserver != null && !connectivityObserver.isOnline) {
            return Result.failure(Exception("Connection required for operational actions."))
        }

        return try {
            val response = incidentApi.updateReportStatus(
                reportId = reportId,
                request = ReportStatusUpdateRequest(status = "IN_PROGRESS", note = "Response started by staff.")
            )
            if (response.isSuccessful && response.body() != null) {
                val detail = response.body()!!.toDomain()
                cachedDetails[reportId] = detail
                Result.success(detail)
            } else {
                Result.failure(Exception(parseError(response.errorBody()?.string())))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Unable to start response action. Check network and try again."))
        }
    }

    override suspend fun resolveReport(reportId: String, note: String?): Result<IncidentDetail> {
        if (connectivityObserver != null && !connectivityObserver.isOnline) {
            return Result.failure(Exception("Connection required for operational actions."))
        }

        return try {
            val response = incidentApi.updateReportStatus(
                reportId = reportId,
                request = ReportStatusUpdateRequest(
                    status = "RESOLVED",
                    note = note ?: "Incident assignment resolved by staff."
                )
            )
            if (response.isSuccessful && response.body() != null) {
                val detail = response.body()!!.toDomain()
                cachedDetails[reportId] = detail
                Result.success(detail)
            } else {
                Result.failure(Exception(parseError(response.errorBody()?.string())))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Unable to resolve incident assignment. Check network and try again."))
        }
    }

    override suspend fun fetchEligibleResponders(reportId: String): Result<List<EligibleResponder>> {
        if (connectivityObserver != null && !connectivityObserver.isOnline) {
            return Result.failure(Exception("Connection required to query eligible responders."))
        }

        return try {
            val response = incidentApi.getEligibleResponders(reportId)
            if (response.isSuccessful) {
                val items = response.body()?.map { it.toDomain() } ?: emptyList()
                Result.success(items)
            } else {
                Result.failure(Exception(parseError(response.errorBody()?.string())))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Unable to fetch eligible responders."))
        }
    }

    override suspend fun fetchAssignedResponders(reportId: String): Result<List<ResponderAssignment>> {
        if (connectivityObserver != null && !connectivityObserver.isOnline) {
            return Result.success(emptyList())
        }

        return try {
            val response = incidentApi.getAssignedResponders(reportId)
            if (response.isSuccessful) {
                val items = response.body()?.map { it.toDomain() } ?: emptyList()
                Result.success(items)
            } else {
                Result.failure(Exception(parseError(response.errorBody()?.string())))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Unable to fetch assigned responders."))
        }
    }

    /**
     * Responder assignment mutations require a live backend connection and are
     * never queued offline because stale dispatch decisions could conflict with
     * another control-room operator.
     */
    override suspend fun assignResponder(reportId: String, responderUserId: String): Result<Unit> {
        if (connectivityObserver != null && !connectivityObserver.isOnline) {
            return Result.failure(Exception("Connection required to assign responders."))
        }

        return try {
            val response = incidentApi.assignResponder(reportId, AssignResponderRequest(responderUserId = responderUserId))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(parseError(response.errorBody()?.string())))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Unable to assign responder."))
        }
    }

    override suspend fun releaseResponder(reportId: String, assignmentId: String): Result<Unit> {
        if (connectivityObserver != null && !connectivityObserver.isOnline) {
            return Result.failure(Exception("Connection required to release responders."))
        }

        return try {
            val response = incidentApi.releaseResponder(reportId, assignmentId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(parseError(response.errorBody()?.string())))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Unable to release responder."))
        }
    }

    override suspend fun fetchMyActiveAssignments(): Result<List<ResponderAssignment>> {
        if (connectivityObserver != null && !connectivityObserver.isOnline) {
            return Result.success(emptyList())
        }

        return try {
            val response = incidentApi.getMyActiveAssignments()
            if (response.isSuccessful) {
                val items = response.body()?.map { it.toDomain() } ?: emptyList()
                Result.success(items)
            } else {
                Result.failure(Exception(parseError(response.errorBody()?.string())))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Unable to fetch active assignments."))
        }
    }

    private fun parseError(errorBody: String?): String {
        if (errorBody.isNullOrBlank()) return "Failed to process operational incident request."
        return try {
            val json = JSONObject(errorBody)
            json.optString("message", "Failed to process operational incident request.")
        } catch (e: Exception) {
            "Failed to process operational incident request."
        }
    }
}
