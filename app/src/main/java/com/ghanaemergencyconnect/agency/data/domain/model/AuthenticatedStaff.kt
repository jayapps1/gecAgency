package com.ghanaemergencyconnect.agency.data.domain.model

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
    val totpEnabled: Boolean = false,
    val roles: Set<String>
) {
    val fullName: String get() = "$firstName $lastName".trim()

    val primaryRole: StaffRole
        get() = StaffRole.fromRoles(roles)
}
