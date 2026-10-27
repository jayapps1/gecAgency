package com.ghanaemergencyconnect.agency.feature.incidents

import com.ghanaemergencyconnect.agency.data.domain.model.OperationalIncident

data class IncidentsUiState(
    val incidents: List<OperationalIncident> = emptyList(),
    val activeFilter: IncidentsFilter = IncidentsFilter.ALL_ACTIVE,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val filteredIncidents: List<OperationalIncident>
        get() = incidents.filter { activeFilter.matches(it.status) }
}
