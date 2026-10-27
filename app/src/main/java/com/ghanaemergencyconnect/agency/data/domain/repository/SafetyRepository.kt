package com.ghanaemergencyconnect.agency.data.domain.repository

import com.ghanaemergencyconnect.agency.data.domain.model.SafetyInformationItem

/**
 * Provides agency-scoped Safety Information operations.
 *
 * Scope is never supplied by arbitrary client input. The authenticated
 * backend session determines which agency records the caller may access.
 */
interface SafetyRepository {
    suspend fun fetchPosts(page: Int = 0): Result<List<com.ghanaemergencyconnect.agency.data.domain.model.SafetyInformationItem>>
    suspend fun createPost(title: String, content: String, category: String, severity: String, notifyUsers: Boolean): Result<Unit>
    suspend fun publishPost(id: String): Result<Unit>
}
