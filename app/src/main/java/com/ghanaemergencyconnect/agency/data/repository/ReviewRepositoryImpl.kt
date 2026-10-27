package com.ghanaemergencyconnect.agency.data.repository

import com.ghanaemergencyconnect.agency.data.api.ReviewApi
import com.ghanaemergencyconnect.agency.data.domain.model.CitizenReviewItem
import com.ghanaemergencyconnect.agency.data.domain.repository.ReviewRepository

class ReviewRepositoryImpl(
    private val reviewApi: ReviewApi
) : ReviewRepository {

    override suspend fun fetchReviews(page: Int): Result<List<CitizenReviewItem>> {
        return try {
            val response = reviewApi.getAdminReviews(page = page)
            if (response.isSuccessful) {
                val list = response.body()?.content?.map { dto ->
                    CitizenReviewItem(
                        id = dto.id ?: "",
                        reportId = dto.reportId,
                        assignmentId = dto.assignmentId,
                        agencyName = dto.agencyName ?: "Agency",
                        stationName = dto.stationName,
                        rating = dto.rating ?: 5,
                        feedback = dto.feedback,
                        createdAt = dto.createdAt ?: ""
                    )
                } ?: emptyList()
                Result.success(list)
            } else {
                Result.failure(Exception("Failed to load citizen reviews."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
