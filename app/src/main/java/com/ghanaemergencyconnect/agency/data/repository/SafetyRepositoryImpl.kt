package com.ghanaemergencyconnect.agency.data.repository

import com.ghanaemergencyconnect.agency.data.api.SafetyApi
import com.ghanaemergencyconnect.agency.data.dto.CreateSafetyInformationRequestDto
import com.ghanaemergencyconnect.agency.data.domain.model.SafetyInformationItem
import com.ghanaemergencyconnect.agency.data.domain.repository.SafetyRepository

/**
 * Implementation of [SafetyRepository] making authenticated API requests.
 */
class SafetyRepositoryImpl(
    private val safetyApi: SafetyApi
) : SafetyRepository {

    override suspend fun fetchPosts(page: Int): Result<List<SafetyInformationItem>> {
        return try {
            val response = safetyApi.getAdminPosts(page = page)
            if (response.isSuccessful) {
                val list = response.body()?.content?.map { dto ->
                    SafetyInformationItem(
                        id = dto.id,
                        agencyId = dto.agencyId,
                        title = dto.title,
                        content = dto.content,
                        category = dto.category ?: "GENERAL",
                        severity = dto.severity ?: "INFO",
                        status = dto.status ?: "DRAFT",
                        notifyUsers = dto.notifyUsers,
                        publishedAt = dto.publishedAt,
                        createdAt = dto.createdAt ?: ""
                    )
                } ?: emptyList()
                Result.success(list)
            } else {
                Result.failure(Exception("Failed to load safety information."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createPost(
        title: String,
        content: String,
        category: String,
        severity: String,
        notifyUsers: Boolean
    ): Result<Unit> {
        return try {
            val request = CreateSafetyInformationRequestDto(
                title = title,
                content = content,
                category = category,
                severity = severity,
                notifyUsers = notifyUsers
            )
            val response = safetyApi.createPost(request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to create safety post."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun publishPost(id: String): Result<Unit> {
        return try {
            val response = safetyApi.publishPost(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to publish safety post."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
