package com.ghanaemergencyconnect.agency.data.domain.model

/**
 * Clean domain model representing an authenticated staff member.
 */
data class AuthenticatedStaff(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String?,
    val agencyId: String?,
    val agencyName: String?,
    val region: String?,
    val stationId: String?,
    val stationName: String?,
    val active: Boolean,
    val locked: Boolean,
    val mustChangePassword: Boolean,
    val roles: Set<String>
) {
    val primaryRole: StaffRole
        get() = StaffRole.fromRoles(roles)

    val displayName: String
        get() = if (firstName.isNotBlank() || lastName.isNotBlank()) {
            "$firstName $lastName".trim()
        } else {
            email
        }

    val primaryRoleLabel: String
        get() = primaryRole.label
}
