package com.ghanaemergencyconnect.agency.feature.reviews

import com.ghanaemergencyconnect.agency.data.domain.model.CitizenReviewItem

data class ReviewsUiState(
    val reviews: List<CitizenReviewItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
