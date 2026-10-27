package com.ghanaemergencyconnect.agency.feature.appliances

import com.ghanaemergencyconnect.agency.data.domain.model.ApplianceStatus
import com.ghanaemergencyconnect.agency.data.domain.model.OperationalAppliance

data class AppliancesUiState(
    val appliances: List<OperationalAppliance> = emptyList(),
    val activeFilter: ApplianceStatus? = null,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val filteredAppliances: List<OperationalAppliance>
        get() = appliances.filter { item ->
            val matchesFilter = activeFilter == null || item.status == activeFilter
            val matchesSearch = searchQuery.isBlank() ||
                    item.callSign.contains(searchQuery, ignoreCase = true) ||
                    item.displayName.contains(searchQuery, ignoreCase = true) ||
                    item.effectiveStationName.contains(searchQuery, ignoreCase = true)
            matchesFilter && matchesSearch
        }
}
