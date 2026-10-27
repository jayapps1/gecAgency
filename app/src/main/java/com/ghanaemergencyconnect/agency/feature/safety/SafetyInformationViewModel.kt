package com.ghanaemergencyconnect.agency.feature.safety

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ghanaemergencyconnect.agency.data.domain.repository.SafetyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SafetyInformationViewModel(
    private val safetyRepository: SafetyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SafetyInformationUiState())
    val uiState: StateFlow<SafetyInformationUiState> = _uiState.asStateFlow()

    fun loadPosts() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val result = safetyRepository.fetchPosts()
            result.fold(
                onSuccess = { list ->
                    _uiState.update { it.copy(isLoading = false, posts = list) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
            )
        }
    }

    fun createAndPublishPost(title: String, content: String, category: String, severity: String, notifyUsers: Boolean) {
        if (_uiState.value.isPublishing) return
        _uiState.update { it.copy(isPublishing = true, errorMessage = null) }
        viewModelScope.launch {
            val createResult = safetyRepository.createPost(title, content, category, severity, notifyUsers)
            createResult.fold(
                onSuccess = {
                    _uiState.update { it.copy(isPublishing = false, successMessage = "Safety post published successfully.") }
                    loadPosts()
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isPublishing = false, errorMessage = error.message) }
                }
            )
        }
    }
}
