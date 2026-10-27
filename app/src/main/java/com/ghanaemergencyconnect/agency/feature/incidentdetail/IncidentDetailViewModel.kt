package com.ghanaemergencyconnect.agency.feature.incidentdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghanaemergencyconnect.agency.data.domain.repository.IncidentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IncidentDetailViewModel(
    private val incidentRepository: IncidentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(IncidentDetailUiState())
    val uiState: StateFlow<IncidentDetailUiState> = _uiState.asStateFlow()

    fun loadDetail(reportId: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val result = incidentRepository.getStaffIncidentDetail(reportId)
            result.fold(
                onSuccess = { detail ->
                    _uiState.update { it.copy(isLoading = false, detail = detail) }
                    loadAssignedResponders(reportId)
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
            )
        }
    }

    fun loadAssignedResponders(reportId: String) {
        viewModelScope.launch {
            val result = incidentRepository.fetchAssignedResponders(reportId)
            result.onSuccess { responders ->
                _uiState.update { it.copy(assignedResponders = responders) }
            }
        }
    }

    fun loadEligibleResponders(reportId: String) {
        _uiState.update { it.copy(isLoadingResponders = true) }
        viewModelScope.launch {
            val result = incidentRepository.fetchEligibleResponders(reportId)
            result.fold(
                onSuccess = { eligible ->
                    _uiState.update { it.copy(isLoadingResponders = false, eligibleResponders = eligible) }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoadingResponders = false) }
                }
            )
        }
    }

    fun assignResponder(reportId: String, responderUserId: String) {
        if (_uiState.value.isPerformingAction) return
        _uiState.update { it.copy(isPerformingAction = true, errorMessage = null) }
        viewModelScope.launch {
            val result = incidentRepository.assignResponder(reportId, responderUserId)
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isPerformingAction = false,
                            actionSuccessMessage = "Responder assigned successfully."
                        )
                    }
                    loadAssignedResponders(reportId)
                    loadEligibleResponders(reportId)
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isPerformingAction = false, errorMessage = error.message) }
                }
            )
        }
    }

    fun releaseResponder(reportId: String, assignmentId: String) {
        if (_uiState.value.isPerformingAction) return
        _uiState.update { it.copy(isPerformingAction = true, errorMessage = null) }
        viewModelScope.launch {
            val result = incidentRepository.releaseResponder(reportId, assignmentId)
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isPerformingAction = false,
                            actionSuccessMessage = "Responder released from assignment."
                        )
                    }
                    loadAssignedResponders(reportId)
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isPerformingAction = false, errorMessage = error.message) }
                }
            )
        }
    }

    fun acknowledgeReport(reportId: String) {
        if (_uiState.value.isPerformingAction) return
        _uiState.update { it.copy(isPerformingAction = true, errorMessage = null) }
        viewModelScope.launch {
            val result = incidentRepository.acknowledgeReport(reportId)
            result.fold(
                onSuccess = { updatedDetail ->
                    _uiState.update {
                        it.copy(
                            isPerformingAction = false,
                            detail = updatedDetail,
                            actionSuccessMessage = "Report acknowledged successfully."
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isPerformingAction = false, errorMessage = error.message) }
                }
            )
        }
    }

    fun startAction(reportId: String) {
        if (_uiState.value.isPerformingAction) return
        _uiState.update { it.copy(isPerformingAction = true, errorMessage = null) }
        viewModelScope.launch {
            val result = incidentRepository.startAction(reportId)
            result.fold(
                onSuccess = { updatedDetail ->
                    _uiState.update {
                        it.copy(
                            isPerformingAction = false,
                            detail = updatedDetail,
                            actionSuccessMessage = "Response action started."
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isPerformingAction = false, errorMessage = error.message) }
                }
            )
        }
    }

    fun resolveReport(reportId: String, note: String?) {
        if (_uiState.value.isPerformingAction) return
        _uiState.update { it.copy(isPerformingAction = true, errorMessage = null) }
        viewModelScope.launch {
            val result = incidentRepository.resolveReport(reportId, note)
            result.fold(
                onSuccess = { updatedDetail ->
                    _uiState.update {
                        it.copy(
                            isPerformingAction = false,
                            detail = updatedDetail,
                            actionSuccessMessage = "Incident assignment resolved."
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isPerformingAction = false, errorMessage = error.message) }
                }
            )
        }
    }
}
