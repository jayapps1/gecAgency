package com.ghanaemergencyconnect.agency.data.domain.repository

import com.ghanaemergencyconnect.agency.data.domain.model.CitizenReviewItem

/**
 * Citizen ratings belong to individual report assignments so each
 * responding agency can receive an independent review for the same
 * public incident.
 */
interface ReviewRepository {
    suspend fun fetchReviews(page: Int = 0): Result<List<com.ghanaemergencyconnect.agency.data.domain.model.CitizenReviewItem>>
}
