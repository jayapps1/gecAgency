package com.ghanaemergencyconnect.agency.feature.incidentdetail

import com.ghanaemergencyconnect.agency.data.domain.model.EligibleResponder
import com.ghanaemergencyconnect.agency.data.domain.model.IncidentDetail
import com.ghanaemergencyconnect.agency.data.domain.model.ResponderAssignment

data class IncidentDetailUiState(
    val detail: IncidentDetail? = null,
    val assignedResponders: List<ResponderAssignment> = emptyList(),
    val eligibleResponders: List<EligibleResponder> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingResponders: Boolean = false,
    val isPerformingAction: Boolean = false,
    val errorMessage: String? = null,
    val actionSuccessMessage: String? = null
)
