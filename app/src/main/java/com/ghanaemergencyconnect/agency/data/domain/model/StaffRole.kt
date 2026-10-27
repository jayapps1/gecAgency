package com.ghanaemergencyconnect.agency.data.domain.model

/**
 * Domain enumeration of all backend staff roles.
 * Unknown roles parse safely to UNKNOWN.
 */
enum class StaffRole(val label: String) {
    SYSTEM_ADMIN("National System Administrator"),
    AGENCY_ADMIN("Agency Administrator"),
    REGIONAL_ADMIN("Regional Administrator"),
    STATION_ADMIN("Station Administrator"),
    CONTROL_ROOM_OPERATOR("Control Room Operator"),
    RESPONDER("Responder"),
    REPORT_REVIEWER("Report Reviewer"),
    AUDITOR("Auditor"),
    UNKNOWN("Staff Member");

    companion object {
        fun fromCode(code: String?): StaffRole {
            if (code.isNullOrBlank()) return UNKNOWN
            return try {
                valueOf(code.uppercase())
            } catch (e: Exception) {
                UNKNOWN
            }
        }

        fun fromRoles(roles: Set<String>): StaffRole {
            return when {
                roles.contains("SYSTEM_ADMIN") -> SYSTEM_ADMIN
                roles.contains("AGENCY_ADMIN") -> AGENCY_ADMIN
                roles.contains("REGIONAL_ADMIN") -> REGIONAL_ADMIN
                roles.contains("STATION_ADMIN") -> STATION_ADMIN
                roles.contains("CONTROL_ROOM_OPERATOR") -> CONTROL_ROOM_OPERATOR
                roles.contains("RESPONDER") -> RESPONDER
                roles.contains("REPORT_REVIEWER") -> REPORT_REVIEWER
                roles.contains("AUDITOR") -> AUDITOR
                else -> UNKNOWN
            }
        }
    }
}
