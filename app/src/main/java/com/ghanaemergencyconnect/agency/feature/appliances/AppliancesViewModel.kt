package com.ghanaemergencyconnect.agency.feature.appliances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghanaemergencyconnect.agency.data.domain.model.ApplianceStatus
import com.ghanaemergencyconnect.agency.data.domain.repository.ApplianceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppliancesViewModel(
    private val applianceRepository: ApplianceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppliancesUiState())
    val uiState: StateFlow<AppliancesUiState> = _uiState.asStateFlow()

    fun loadAppliances() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val result = applianceRepository.fetchAppliances()
            result.fold(
                onSuccess = { list ->
                    _uiState.update { it.copy(isLoading = false, appliances = list) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
            )
        }
    }

    fun selectFilter(status: ApplianceStatus?) {
        _uiState.update { it.copy(activeFilter = status) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun updateStatus(id: String, newStatus: String, reason: String?) {
        viewModelScope.launch {
            applianceRepository.updateStatus(id, newStatus, reason)
            loadAppliances()
        }
    }
}
